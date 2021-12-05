package com.portfolio.data.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.entity.Asset;
import com.portfolio.entity.Portfolio;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetVO extends RepresentationModel<AssetVO> implements Serializable {
    private Long id;

    @NotEmpty(message = "Campo code obrigatório.")
    private String code;

    @NotEmpty(message = "Campo nome obrigatório.")
    private String name;

    public static AssetVO create(Asset asset) { return new ModelMapper().map(asset, AssetVO.class); }

    @Override
    public int hashCode() {
        return Math.toIntExact(id) * code.hashCode() * name.hashCode();
    }
}
