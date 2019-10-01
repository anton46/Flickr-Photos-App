package com.photos.app

import org.junit.Assert
import org.mockito.Mockito

infix fun Any?.shouldEqual(theOther: Any?) = Assert.assertEquals(theOther, this)

infix fun Any?.shouldNotEqual(theOther: Any?) = Assert.assertNotEquals(theOther, this)

fun <T> any(): T = Mockito.any<T>()