package com.shakeel.repository;

import org.springframework.data.repository.CrudRepository;
import com.shakeel.model.*;

public interface LocationRepository extends CrudRepository<UserLocation, Long> {

}
