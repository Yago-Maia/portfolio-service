package com.portfolio.controller;

import com.portfolio.data.vo.AssetApiVO;
import com.portfolio.data.vo.AssetVO;
import com.portfolio.data.vo.InsertAssetVO;
import com.portfolio.data.vo.PortfolioVO;
import com.portfolio.service.CallAssetsService;
import com.portfolio.service.PortfolioService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequiredArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;
    private final PagedResourcesAssembler<PortfolioVO> assembler;

    @Operation(summary = "Procurar carteira por Id", description = "Endpoint para recuperação de carteira pelo Id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Carteira encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Carteira não encontrada") })
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public PortfolioVO findById(@PathVariable("id") @ApiParam("Id da carteira")  Long id) {
        PortfolioVO portfolioVO = portfolioService.findById(id);
        portfolioVO.add(linkTo(methodOn(PortfolioController.class).findById(id)).withSelfRel());
        return portfolioVO;
    }

    @Operation(summary = "Buscar todas as carteiras", description = "Endpoint para recuperação de todas as carteiras")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cateiras encontradas"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Nenhuma carteira cadastrada") })
    @GetMapping
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") @ApiParam("Número da página") int page,
                                     @RequestParam(value = "limit", defaultValue = "12") @ApiParam("Limite de resultado") int limit,
                                     @RequestParam(value = "direction", defaultValue = "asc") @ApiParam("Direção da ordenação") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        Page<PortfolioVO> portfolioVOS = portfolioService.findAll(pageable);
        portfolioVOS.stream()
                .forEach(p -> p.add(linkTo(methodOn(PortfolioController.class).findById(p.getId())).withSelfRel()));
        PagedModel<EntityModel<PortfolioVO>> pagedModel = assembler.toModel(portfolioVOS);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @Operation(summary = "Criar carteira", description = "Endpoint para criar carteira")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Carteira criada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido")})
    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    @ResponseStatus(HttpStatus.CREATED)
    PortfolioVO create(@RequestBody @Valid PortfolioVO portfolioVO) {
        PortfolioVO portfolioVOSaved = portfolioService.create(portfolioVO);
        portfolioVOSaved.add(linkTo(methodOn(PortfolioController.class).findById(portfolioVOSaved.getId())).withSelfRel());

        return portfolioVOSaved;
    }

    @Operation(summary = "Atualizar carteira", description = "Endpoint para editar carteira")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Carteira editada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Carteira não encontrada") })
    @PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    PortfolioVO update(@RequestBody @Valid PortfolioVO portfolioVO) {
        PortfolioVO portfolioVOSaved = portfolioService.update(portfolioVO);
        portfolioVOSaved.add(linkTo(methodOn(PortfolioController.class).findById(portfolioVOSaved.getId())).withSelfRel());

        return portfolioVOSaved;
    }

    @Operation(summary = "Deletar carteira", description = "Endpoint para deletar carteira")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Carteira deletada"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Carteira não encontrada") })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") @ApiParam("Id da carteira") Long id) {
        portfolioService.delete(id);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Inserir ação na carteira", description = "Endpoint para inserir ação na carteira")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ação inserida"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido")})
    @PostMapping(path = "/addAsset",produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public PortfolioVO addAsset(@RequestBody @Valid InsertAssetVO insertAssetVO) {
        PortfolioVO portfolioVOUpdated = portfolioService.addAsset(insertAssetVO);
        portfolioVOUpdated.add(linkTo(methodOn(PortfolioController.class).findById(portfolioVOUpdated.getId())).withSelfRel());

        return portfolioVOUpdated;
    }

    @Operation(summary = "Remover ação da carteira", description = "Endpoint para remover ação da carteira")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ação removida"),
            @ApiResponse(responseCode = "401", description = "Não autorizado"),
            @ApiResponse(responseCode = "403", description = "Recurso proibido"),
            @ApiResponse(responseCode = "404", description = "Ação ou carteira não encontrada") })
    @DeleteMapping("/{id}/asset/{code}")
    public PortfolioVO removeAsset(
            @PathVariable("id") @ApiParam("Id da carteira") Long id,
            @PathVariable("code") @ApiParam("Código da ação") String code) {

        return portfolioService.removeAsset(id, code);
    }
}
