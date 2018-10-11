package org.skk.domain

import io.ebean.Model
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name="account")
class Account(@Id val id: Int, val name: String) : Model() {

}
