package com.portfolio.service;

import com.portfolio.data.vo.AssetVO;
import com.portfolio.data.vo.InsertAssetVO;
import com.portfolio.data.vo.PortfolioVO;
import com.portfolio.entity.Portfolio;
import com.portfolio.exception.NotAllowedException;
import com.portfolio.exception.ResourceNotFoundException;
import com.portfolio.repository.AssetRepository;
import com.portfolio.repository.PortfolioRepository;
import com.portfolio.security.UserSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.util.Objects;
import java.util.Optional;

import static com.portfolio.constants.Messages.*;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final AssetRepository assetRepository;

    @Autowired
    public PortfolioService(PortfolioRepository portfolioRepository, AssetRepository assetRepository) {
        this.portfolioRepository = portfolioRepository;
        this.assetRepository = assetRepository;
    }

    public PortfolioVO create(PortfolioVO portfolioVO) {
        portfolioVO.setIdUser(Objects.requireNonNull(UserSS.authenticated()).getIdUser());
        return PortfolioVO.create(portfolioRepository.save(Portfolio.create(portfolioVO)));
    }

    public Page<PortfolioVO> findAll(Pageable pageable) {
        var page = portfolioRepository.findAll(pageable);
        return page.map(this::convertToPortfolioVO);
    }

    private PortfolioVO convertToPortfolioVO(Portfolio portfolio){ return PortfolioVO.create(portfolio); }

    public PortfolioVO findById(Long id) {
        var entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResolutionException(ID_PORTFOLIO_NOT_FOUND));

        this.authenticated(entity.getIdUser());

        return PortfolioVO.create(entity);
    }

    public PortfolioVO update(PortfolioVO portfolioVO) {

        final Optional<Portfolio> optionalPortfolio = portfolioRepository.findById(portfolioVO.getId());

        if(optionalPortfolio.isEmpty()) {
            throw new ResolutionException(ID_PORTFOLIO_NOT_FOUND);
        }

        this.authenticated(optionalPortfolio.get().getIdUser());

        return PortfolioVO.create(portfolioRepository.save(Portfolio.create(portfolioVO)));
    }

    public void delete(Long id) {
        var entity = portfolioRepository.findById(id)
                .orElseThrow(() -> new ResolutionException(ID_PORTFOLIO_NOT_FOUND));

        this.authenticated(entity.getIdUser());

        portfolioRepository.delete(entity);
    }

    public PortfolioVO addAsset(InsertAssetVO insertAssetVO) {
        PortfolioVO portfolioVO = this.findById(insertAssetVO.getIdPortfolio());

        var entity = AssetVO.create(assetRepository.findByCode(insertAssetVO.getCodeAsset())
                .orElseThrow(() -> new ResourceNotFoundException(ID_ASSET_NOT_FOUND)));

        this.authenticated(portfolioVO.getIdUser());

        portfolioVO.getHasAssets().add(entity);

        return this.update(portfolioVO);
    }

    public PortfolioVO removeAsset(Long id, String code) {
        PortfolioVO portfolioVO = this.findById(id);

        var entity = AssetVO.create(assetRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException(ID_ASSET_NOT_FOUND)));

        this.authenticated(portfolioVO.getIdUser());

        portfolioVO.getHasAssets().remove(entity);

        return this.update(portfolioVO);
    }

    private void authenticated(Long id) {
        UserSS userSS = UserSS.authenticated();
        assert userSS != null;
        if(!Objects.equals(userSS.getIdUser(), id)) {
            throw new NotAllowedException(NOT_ALLOWED_PORTFOLIO_UPDATE);
        }
    }
}
