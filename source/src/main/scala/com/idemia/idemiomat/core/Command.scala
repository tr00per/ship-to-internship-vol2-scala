package com.idemia.idemiomat.core

trait Command[R] {
  def execute(imat: Idemiomat): R
}
