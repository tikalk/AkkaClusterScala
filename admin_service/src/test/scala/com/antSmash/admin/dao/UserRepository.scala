package com.antSmash.admin.dao

import org.springframework.data.repository.CrudRepository
import java.lang.Long

import com.antSmash.admin.model.User
import org.springframework.stereotype.Repository

@Repository
trait UserRepository extends CrudRepository[User, Long]