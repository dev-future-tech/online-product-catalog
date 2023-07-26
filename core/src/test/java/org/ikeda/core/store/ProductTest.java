package org.ikeda.core.store;

import jakarta.persistence.*;
import org.ikeda.store.core.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({DatabaseSetupExtension.class, CDIExtension.class})
public class ProductTest {

    private final Logger log = Logger.getLogger(ProductTest.class.getCanonicalName());

    @PersistenceUnit
    EntityManagerFactory emf;


    @Test
    void createProductTest() {
        EntityManager em = this.emf.createEntityManager();

        Product product = new Product();
        product.setName("Apples");
        product.setSku("F43289");
        product.setDescription("Fresh fruit that customers cook into pies");
        EntityTransaction txn = em.getTransaction();
        txn.begin();
        em.persist(product);
        txn.commit();

        assertThat(product.getProductId()).isNotNull().isGreaterThan(1);
        assertThat(product.getName()).isEqualTo("Apples");

        log.log(Level.INFO, "ProductId is {0}", product.getProductId());
    }

    @Test
    void findCreatedProductTest() {
        EntityManager em = this.emf.createEntityManager();

        Product product = new Product();
        product.setName("Bananas");
        product.setSku("F5498");
        product.setDescription("Yellow fruit that is made to make bread");
        EntityTransaction txn = em.getTransaction();
        txn.begin();
        em.persist(product);
        txn.commit();

        Long productId = product.getProductId();

        assertThat(productId).isNotNull().isGreaterThan(1);


        Product found = em.createNamedQuery("findById", Product.class)
                .setParameter("productId", productId).getSingleResult();

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo(product.getName());
    }

    @Test
    void testDeleteProduct() {
        EntityManager em = emf.createEntityManager();

        Product product = new Product();
        product.setName("Strawberry");
        product.setSku("F34590");
        product.setDescription("What some people consider a fruit but in actual fact it is not");

        EntityTransaction txn = em.getTransaction();
        txn.begin();
        em.persist(product);
        txn.commit();

        Long productId = product.getProductId();
        log.log(Level.INFO, "New productId for product {0} is {1}", new Object[]{product.getName(), product.getProductId()});

        assertThat(productId).isNotNull().isGreaterThan(1);

        Product found = em.createNamedQuery("findById", Product.class).setParameter("productId", productId).getSingleResult();
        assertThat(found).isNotNull();
        txn.begin();
        em.remove(product);
        txn.commit();

        try {
            Product foundAgain = em.createNamedQuery("findById", Product.class).setParameter("productId", productId).getSingleResult();
            assertThat(foundAgain).isNull();
        } catch (NoResultException nre) {
            assertThat(nre).isNotNull();
        }
    }

    @Test
    void testUpdateProduct() {
        Product product = new Product();
        product.setName("Gooseberry");
        product.setSku("F89504");
        product.setDescription("Some weird purple berry thing");

        EntityManager em = emf.createEntityManager();
        EntityTransaction txn = em.getTransaction();

        txn.begin();
        em.persist(product);
        txn.commit();

        assertThat(product.getProductId()).isNotNull().isGreaterThan(1);

        String newDescription = "Actually let's describe it as something else";
        product.setDescription(newDescription);

        txn.begin();
        em.persist(product);
        txn.commit();

        Product updatedProduct = em.createNamedQuery("findById", Product.class).setParameter("productId", product.getProductId()).getSingleResult();

        assertThat(updatedProduct.getDescription()).isEqualTo(newDescription);
    }
}
