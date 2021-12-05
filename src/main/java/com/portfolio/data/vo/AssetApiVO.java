package com.portfolio.data.vo;

import com.portfolio.entity.Asset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetApiVO extends RepresentationModel<AssetApiVO> {
    private Long id;
    private String code;
    private String name;
    private String category;
    private String data_source;
    private String last_close;
    private String last_change;
    private String last_change_7_days;
    private String last_change_30_days;
    private String last_change_90_days;
    private String last_change_180_days;
    private String year_change;

    public static AssetVO mapperAssetApi(AssetApiVO assetApiVO) {
        return AssetVO.builder()
                .code(assetApiVO.getCode())
                .name(assetApiVO.getName())
                .id(assetApiVO.getId())
                .build();
    }

}
