package com.itau.pix.pix.service;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.exception.NotFoundException;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import com.itau.pix.pix.repository.ContaPixRepository;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.validator.PixValidationStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContaPixService {

  @Autowired
  private ContaPixRepository contaPixRepository;
  @Autowired
  private PixValidationStrategy pixValidationStrategy;

  public ContaPix save(ContaPixCreateDTO dto) {
    pixValidationStrategy.requestValidator(dto);

    ContaPix contaPix = new ContaPix();
    BeanUtils.copyProperties(dto, contaPix);
    contaPix.setDataInclusao(LocalDateTime.now());
    contaPix.setAtiva(Boolean.TRUE);

    return contaPixRepository.saveAndFlush(contaPix);
  }

  public ContaPix update(UUID id,ContaPixUpdateDTO dto) {
    ContaPix contaPix = contaPixRepository.findById(id).orElseThrow(() -> new NotFoundException("Não encontrado"));
    pixValidationStrategy.updateValidator(contaPix, dto);
    BeanUtils.copyProperties(dto, contaPix, "id");
        return contaPixRepository.save(contaPix);
  }

  public Optional<ContaPix> findById(UUID id) {
    return contaPixRepository.findById(id);
  }

  public Page<ContaPix> findByCriteria(Optional<UUID> id, Optional<TipoChaveEnum> tipoChave, Optional<String> numAgencia, Optional<String> numConta, Optional<String> nome, Optional<LocalDateTime> dataInclusao, Optional<LocalDateTime> dataInativacao,  Pageable pageable) {
    List<ContaPix> resultList;
    if(id.isPresent()) {
      Optional<ContaPix> result = findById(id.get());
      resultList = new ArrayList<>();
      result.ifPresent(resultList::add);
    } else {
      if(tipoChave.isPresent()) {
        if(numAgencia.isPresent() && numConta.isPresent()) {
          if(dataInclusao.isPresent()) {
            resultList = contaPixRepository.findByTipoChaveAndNumAgenciaAndNumContaAndNomeAndDataInclusao(tipoChave.get(), numAgencia.get(), numConta.get(), nome.get(), dataInclusao.get());
          } else if (dataInativacao.isPresent()) {
            resultList = contaPixRepository.findByTipoChaveAndNumAgenciaAndNumContaAndNomeAndDataInativacao(tipoChave.get(), numAgencia.get(), numConta.get(), nome.get(), dataInativacao.get());
          } else {
            resultList = contaPixRepository.findByTipoChaveAndNumAgenciaAndNumConta(tipoChave.get(), numAgencia.get(), numConta.get());
          }
        } else {
          resultList = contaPixRepository.findByTipoChave(tipoChave.get());
        }
      } else if (numAgencia.isPresent() && numConta.isPresent()) {
        resultList = contaPixRepository.findByNumAgenciaAndNumConta(numAgencia.get(), numConta.get());
      } else if (nome.isPresent()) {
        resultList = contaPixRepository.findByNome(nome.get());
      } else if (dataInclusao.isPresent()) {
        resultList = contaPixRepository.findByDataInclusao(dataInclusao.get());
      } else if (dataInativacao.isPresent()) {
        resultList = contaPixRepository.findByDataInativacao(dataInativacao.get());
      } else {
        resultList = new ArrayList<>();
      }
    }

    if(resultList.isEmpty()) {
      throw new NotFoundException("Nenhuma chave encontrada");
    }

    return new PageImpl<>(resultList);
  }

  public ContaPix disableById(UUID id) {
    ContaPix contaPix = contaPixRepository.findById(id).orElseThrow(() -> new NotFoundException("Não encontrado"));

    pixValidationStrategy.isActive(contaPix);
    contaPix.setAtiva(Boolean.FALSE);
    contaPix.setDataInativacao(LocalDateTime.now());
    return contaPixRepository.save(contaPix);
  }

}
