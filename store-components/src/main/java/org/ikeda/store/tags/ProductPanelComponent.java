package org.ikeda.store.tags;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UICommand;
import org.ikeda.store.core.Product;

@FacesComponent(value="ProductPanel")
public class ProductPanelComponent extends UICommand {


    private Long productId;

    private String styleClass;

    private Product product;

    @Override
    public String getFamily() {
        return "Product";
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
