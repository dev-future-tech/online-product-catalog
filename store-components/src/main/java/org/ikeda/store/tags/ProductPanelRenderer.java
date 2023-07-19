package org.ikeda.store.tags;

import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;
import jakarta.inject.Inject;
import org.ikeda.store.core.Product;
import org.ikeda.store.service.ProductService;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@FacesRenderer(componentFamily = "Product", rendererType = "ProductPanel")
public class ProductPanelRenderer extends Renderer<ProductPanelComponent> {

    private final Logger log = Logger.getLogger(ProductPanelRenderer.class.getCanonicalName());

    @Inject
    ProductService service;

    @Override
    public void encodeBegin(FacesContext context, ProductPanelComponent component) throws IOException {
        log.log(Level.FINE, "Rendering product Panel for product {0}", component.getProductId());

//        Product product = this.service.findProductById(component.getProductId());

        Product product = component.getProduct();
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("class", component.getStyleClass(), "class");

        writer.startElement("h1", component);
        writer.writeText(product.getName(), component, "name");
        writer.endElement("h1");

        writer.startElement("div", component);
        writer.writeText(product.getDescription(), component, "description");
        writer.endElement("div");
    }

    @Override
    public void encodeChildren(FacesContext context, ProductPanelComponent component) throws IOException {
        if ((context == null) || (component == null)) {
            throw new NullPointerException();
        }
    }

    @Override
    public void encodeEnd(FacesContext context, ProductPanelComponent component) throws IOException {
        log.log(Level.FINE, "Finishing rendering product Panel for product {0}", component.getProductId());
        ResponseWriter writer = context.getResponseWriter();
        writer.endElement("div");
    }
}
