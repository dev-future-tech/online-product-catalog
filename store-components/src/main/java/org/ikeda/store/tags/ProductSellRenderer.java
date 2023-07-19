package org.ikeda.store.tags;

import jakarta.faces.context.FacesContext;
import jakarta.faces.render.FacesRenderer;
import jakarta.faces.render.Renderer;

import java.io.IOException;

@FacesRenderer(componentFamily = "Product", rendererType = "ProductDetails")
public class ProductSellRenderer  extends Renderer<ProductSellComponent> {
    @Override
    public void encodeBegin(FacesContext context, ProductSellComponent component) throws IOException {
        super.encodeBegin(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, ProductSellComponent component) throws IOException {
        super.encodeEnd(context, component);
    }
}
