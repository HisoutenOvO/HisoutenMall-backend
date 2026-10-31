package cn.hisouten.mall.service.impl;

import cn.hisouten.mall.common.pojo.PageResult;
import cn.hisouten.mall.dao.mapper.ProductMapper;
import cn.hisouten.mall.dao.mapper.ProductSkuMapper;
import cn.hisouten.mall.exception.BizException;
import cn.hisouten.mall.pojo.doc.ProductDoc;
import cn.hisouten.mall.pojo.dto.product.UserProductPageQueryDTO;
import cn.hisouten.mall.pojo.entity.Product;
import cn.hisouten.mall.pojo.entity.ProductSku;
import cn.hisouten.mall.pojo.vo.product.UserProductPageResultVO;
import cn.hisouten.mall.service.ProductSearchService;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.highlight.Highlight;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightField;
import org.springframework.data.elasticsearch.core.query.highlight.HighlightParameters;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.hisouten.mall.constant.ExceptionMessageConstant.PRICE_ERROR;
import static cn.hisouten.mall.constant.StatusConstant.ENABLED;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final ProductMapper productMapper;
    private final ProductSkuMapper productSkuMapper;

    @Override
    public PageResult<UserProductPageResultVO> userPageQuery(UserProductPageQueryDTO dto) {
        // 1. 基础校验：金额区间
        if (dto.getMinPrice() != null && dto.getMaxPrice() != null) {
            if (dto.getMinPrice().compareTo(dto.getMaxPrice()) > 0) {
                throw new BizException(PRICE_ERROR);
            }
        }

        // 2. 构建布尔查询
        BoolQuery.Builder boolQuery = new BoolQuery.Builder();
        boolean hasKeyword = StringUtils.hasText(dto.getKeyword());

        // 2. 关键词匹配
        if (hasKeyword) {
            boolQuery.must(m -> m.match(mm -> mm
                    .field("all")
                    .query(dto.getKeyword())
            ));
        } else {
            boolQuery.must(m -> m.matchAll(ma -> ma));
        }

        // 2.2 过滤条件
        boolQuery.filter(f -> f.term(t -> t.field("status").value(1))); // 仅上架
        if (dto.getCategoryId() != null) {
            boolQuery.filter(f -> f.term(t -> t.field("categoryId").value(dto.getCategoryId())));
        }
        if (dto.getBrandId() != null) {
            boolQuery.filter(f -> f.term(t -> t.field("brandId").value(dto.getBrandId())));
        }
        if (dto.getMerchantId() != null) {
            boolQuery.filter(f -> f.term(t -> t.field("merchantId").value(dto.getMerchantId())));
        }

        // 2.3 价格区间 (使用 JsonData.of 替代 Number.of)
        if (dto.getMinPrice() != null || dto.getMaxPrice() != null) {
            boolQuery.filter(f -> f.range(r -> {
                r.field("minPrice");
                if (dto.getMinPrice() != null) {
                    r.gte(JsonData.of(dto.getMinPrice()));
                }
                if (dto.getMaxPrice() != null) {
                    r.lte(JsonData.of(dto.getMaxPrice()));
                }
                return r;
            }));
        }

        // 3. 构建查询对象并分页
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder()
                .withQuery(boolQuery.build()._toQuery())
                .withPageable(PageRequest.of(dto.getPage() - 1, dto.getPageSize()));

        // 4. 排序
        int sortType = dto.getSortType() != null ? dto.getSortType() : 0;
        switch (sortType) {
            case 1: // 销量
                nativeQueryBuilder.withSort(Sort.by(Sort.Direction.DESC, "sales"));
                break;
            case 2: // 价格升
                nativeQueryBuilder.withSort(Sort.by(Sort.Direction.ASC, "minPrice"));
                break;
            case 3: // 价格降
                nativeQueryBuilder.withSort(Sort.by(Sort.Direction.DESC, "minPrice"));
                break;
            case 4: // 新品
                nativeQueryBuilder.withSort(Sort.by(Sort.Direction.DESC, "createTime"));
                break;
            case 0: // 综合
            default:
                if (!hasKeyword) {
                    nativeQueryBuilder.withSort(Sort.by(Sort.Direction.DESC, "sales"));
                }
                break;
        }

        // 5. 高亮设置
        if (hasKeyword) {
            // 1. 构建高亮参数 (前后缀标签)
            HighlightParameters parameters = HighlightParameters.builder()
                    .withPreTags("<span style='color:#e1251b;font-weight:bold'>")
                    .withPostTags("</span>")
                    .build();

            // 2. 指定高亮字段 (直接 new)
            HighlightField field = new HighlightField("name");

            // 3. 构建高亮对象
            Highlight highlight = new Highlight(parameters, java.util.List.of(field));

            // 4. 设置到 QueryBuilder
            nativeQueryBuilder.withHighlightQuery(
                    new HighlightQuery(highlight, ProductDoc.class)
            );
        }

        // 6. 执行查询
        SearchHits<ProductDoc> searchHits = elasticsearchOperations.search(
                nativeQueryBuilder.build(), ProductDoc.class);

        // 7. 解析结果
        List<UserProductPageResultVO> records = new ArrayList<>();
        for (SearchHit<ProductDoc> hit : searchHits) {
            ProductDoc doc = hit.getContent();
            UserProductPageResultVO vo = new UserProductPageResultVO();
            BeanUtils.copyProperties(doc, vo);
            vo.setId(Long.valueOf(doc.getId()));
            vo.setSalesCount(doc.getSales());

            // 提取高亮字段覆盖原名
            if (hasKeyword && hit.getHighlightFields().containsKey("name")) {
                vo.setName(hit.getHighlightFields().get("name").getFirst());
            }
            records.add(vo);
        }

        // 8. 返回
        return new PageResult<>(searchHits.getTotalHits(), records);
    }

    @Override
    public void syncToEs(Long productId) {
        // 1. 查数据库
        Product product = productMapper.selectByIdIgnoreLogic(productId);
        if (product == null || product.getDeleted() == 1 || product.getStatus() == 0) {
            // 如果是被删除或下架的商品，直接从 ES 删掉
            this.deleteById(productId);
            return;
        }

        // 2. 查 SKU 计算最低价和总库存
        List<ProductSku> skus = productSkuMapper.selectByProductId(productId);
        List<ProductSku> skuList = skus.stream().filter(sku -> sku.getStatus() == ENABLED).toList();

        BigDecimal minPrice = BigDecimal.ZERO; // 计算最低价
        Integer totalStock = 0;  // 计算总库存

        if (!skuList.isEmpty()) {
            minPrice = skuList.getFirst().getPrice();
            for (ProductSku sku : skuList) {
                if (sku.getPrice().compareTo(minPrice) < 0) {
                    minPrice = sku.getPrice();
                }
                totalStock += sku.getStock();
            }
        }

        // 3. 组装 ProductDoc
        ProductDoc doc = new ProductDoc();
        BeanUtils.copyProperties(product, doc);
        doc.setId(productId.toString());
        doc.setMinPrice(minPrice);
        doc.setTotalStock(totalStock);
        //销量通过定时任务更新

        // 4. 保存到 ES
        elasticsearchOperations.save(doc);
    }

    /**
     * 删除文档
     * @param productId 商品id
     */
    @Override
    public void deleteById(Long productId) {
        elasticsearchOperations.delete(String.valueOf(productId), ProductDoc.class);
    }

    /**
     * 更新ES文档的销量数据
     * @param productId 商品id
     * @param totalSales 总销量
     */
    public void updateSalesInEs(Long productId, Integer totalSales) {
        // 使用 Map 来存放要更新的字段
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("sales", totalSales);

        // 构建局部更新查询
        UpdateQuery updateQuery = UpdateQuery.builder(String.valueOf(productId))
                .withDocument(Document.from(updateMap))
                .build();

        try {
            elasticsearchOperations.update(updateQuery, elasticsearchOperations.getIndexCoordinatesFor(ProductDoc.class));
        } catch (Exception e) {
            log.error("更新商品销量失败, productId: {}", productId, e);
        }
    }
}