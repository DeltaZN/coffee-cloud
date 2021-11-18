package ru.itmo

import org.junit.Test
import org.springframework.web.client.RestTemplate
import ru.itmo.coffee.dto.IngredientDTO

class HelloTest {
    @Test
    fun test() {
        val restTemplate = RestTemplate()
        val fooResourceUrl = "http://localhost:8090/api/store/ingredients"
        val response = restTemplate.getForObject(fooResourceUrl, Array<IngredientDTO>::class.java)
        println()
    }

}
