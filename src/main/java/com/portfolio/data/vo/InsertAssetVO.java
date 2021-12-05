package com.portfolio.data.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class InsertAssetVO {

    @NotNull(message = "Campo idPortfolio é obrigatório.")
    private Long idPortfolio;

    @NotEmpty(message = "Campo codeAsset é obrigatório.")
    private String codeAsset;
}
