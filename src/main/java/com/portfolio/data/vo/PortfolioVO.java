package com.portfolio.data.vo;

import com.portfolio.entity.Asset;
import com.portfolio.entity.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioVO extends RepresentationModel<PortfolioVO> implements Serializable {

    private Long id;

    @Column(length = 30, nullable = false)
    @NotEmpty(message = "Campo name obrigatório.")
    private String name;

    private Long idUser;

    @Nullable
    Set<AssetVO> hasAssets = new HashSet<>();

    public static PortfolioVO create(Portfolio portfolio) { return new ModelMapper().map(portfolio, PortfolioVO.class); }
}
