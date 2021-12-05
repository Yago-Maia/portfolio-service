package com.portfolio.entity;

import com.portfolio.data.vo.PortfolioVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    @NotEmpty(message = "Campo name obrigatório.")
    private String name;

    @NotNull(message = "Campo idUser obrigatório.")
    private Long idUser;

    @ManyToMany
    @JoinTable(name = "asset_portfolio",
                joinColumns = @JoinColumn(name = "portfolio_id"),
                inverseJoinColumns = @JoinColumn(name = "asset_id"))
    Set<Asset> hasAssets = new HashSet<>();

    public static Portfolio create(PortfolioVO portfolioVO){ return new ModelMapper().map(portfolioVO, Portfolio.class); }

}
