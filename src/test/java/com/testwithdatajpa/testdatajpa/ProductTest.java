package com.testwithdatajpa.testdatajpa;

import com.testwithdatajpa.testdatajpa.model.Product;
import com.testwithdatajpa.testdatajpa.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@DataJpaTest//contém configurações que inicializa um contexto da aplicação muito reduzido, usando essa anotação
//apenas o que for relacionado com spring data jpa será iniciaizado
//A dataJpaTest está configurado para ignorar qualquer configuração de banco de dados que você tenha na aplicação
//e está configurado para configurar automaticamente um banco H2, Derby ou HSQL para você, basta apenas
//ter uma dessas dependencias de banco de dados no seu pom.xml
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE, connection = EmbeddedDatabaseConnection.H2)//replace.none indica que você
//não deseja que outro banco de dados configurado na sua aplicação seja substituído no seu teste.
// a connection você inidica qual banco você quer utilizar no seu teste. No caso escolhi a h2, pois
//na minha dependência no pom.xml tem h2.
public class ProductTest {

    @Autowired
    private ProductRepository productRepository;

    @AfterEach
    public void cleanAll() {
        productRepository.deleteAll();
    }

    @Test
    @Sql(statements = "insert into product (name, price) values ('Mouse', 15)")
    public void findByNameIgnoreCaseReturnAProduct() {
        final String NAME_ESPERADO = "Mouse";
        final Integer PRECO_ESPERADO = 15;

        Product product = productRepository.findByNameIgnoreCase("mouse");

        Assertions.assertThat(product.getName()).isEqualTo(NAME_ESPERADO);
        Assertions.assertThat(product.getPrice()).isEqualTo(PRECO_ESPERADO);
    }

    @Test
    @Sql(statements = "insert into product (name, price) values ('Mouse', 15)")
    @Sql(statements = "insert into product (name, price) values ('Scanner', 40)")
    public void findAllProductsOrderedAscendentByPriceReturnAnOrderedListOfProducts() {
        Product product = new Product("", 60);
        List<Product> productList = productRepository.findAllOrderedByPriceAsc();

        Assertions.assertThat(productList).extracting(Product::getName).containsExactly("Mouse", "Scanner");
    }
}
