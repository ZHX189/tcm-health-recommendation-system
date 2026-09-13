package com.tcm.model.vo;

import com.tcm.model.entity.HealthArticle;
import com.tcm.model.entity.HealthPlan;
import com.tcm.model.entity.Product;
import lombok.Data;

import java.util.List;

/**
 * 个性化推荐结果VO
 *
 * @author Ti
 * @since 2026-03-27
 */
@Data
public class RecommendationVO {

    /**
     * 是否已填写健康档案
     */
    private boolean hasHealthRecord;

    /**
     * 用户体质类型
     */
    private String constitutionType;

    /**
     * 当前季节
     */
    private String currentSeason;

    /**
     * 体质推荐 - 商品
     */
    private List<Product> constitutionProducts;

    /**
     * 体质推荐 - 养生方案
     */
    private List<HealthPlan> constitutionPlans;

    /**
     * 体质推荐 - 养生文章
     */
    private List<HealthArticle> constitutionArticles;

    /**
     * 行为推荐 - 猜你喜欢的商品
     */
    private List<Product> behaviorProducts;

    /**
     * 季节推荐 - 应季商品
     */
    private List<Product> seasonalProducts;

    /**
     * 季节推荐 - 应季方案
     */
    private List<HealthPlan> seasonalPlans;
}
