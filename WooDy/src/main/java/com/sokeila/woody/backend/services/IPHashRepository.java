package com.sokeila.woody.backend.services;

import org.springframework.data.repository.CrudRepository;

import com.sokeila.woody.backend.entity.IPHash;

public interface IPHashRepository extends CrudRepository<IPHash, Long> {

	IPHash findByIpHash(String ipHash);
}
