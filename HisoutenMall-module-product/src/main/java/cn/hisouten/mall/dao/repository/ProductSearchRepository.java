package cn.hisouten.mall.dao.repository;

import cn.hisouten.mall.pojo.doc.ProductDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSearchRepository extends ElasticsearchRepository<ProductDoc,Long> {
}
