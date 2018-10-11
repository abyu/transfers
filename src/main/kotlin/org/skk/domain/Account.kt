package org.skk.domain

import io.ebean.Model
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="account")
data class Account(
        @Id val id: Long = 0,
        val name: String
       ) : Model()

