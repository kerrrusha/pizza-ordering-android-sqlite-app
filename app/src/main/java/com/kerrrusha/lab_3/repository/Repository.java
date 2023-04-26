package com.kerrrusha.lab_3.repository;

import java.util.List;

public interface Repository<T> {

    List<T> findAll();

}
