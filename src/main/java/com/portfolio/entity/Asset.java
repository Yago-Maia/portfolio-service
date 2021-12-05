package com.portfolio.entity;

import com.portfolio.data.vo.AssetApiVO;
import com.portfolio.data.vo.AssetVO;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 8, nullable = false, unique = true)
    @NotEmpty(message = "Campo code obrigatório.")
    private String code;

    @Column(length = 200, nullable = false)
    @NotEmpty(message = "Campo nome obrigatório.")
    private String name;

    @Nullable
    @ManyToMany(mappedBy = "hasAssets")
    Set<Portfolio> inPortfolios = new HashSet<>();

    public static Asset create(AssetVO assetVO){ return new ModelMapper().map(assetVO, Asset.class); }

}
