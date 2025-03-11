package com.tiendatextil.dto;
import com.tiendatextil.model.Stock;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDTO {
    private Long id;
    private Long idArticulo;
    private String nombreProducto;
    private String talla;
    private String color;
    private int cantidad;
    private String nombreAlmacen;
    private Long idAlmacen;  // Agregado

    // Constructor vacío necesario para Jackson
    public StockDTO() {
    }

    public StockDTO(Stock stock) {
        this.id = stock.getId();
        this.idArticulo = stock.getArticulo().getId();
        this.nombreProducto = stock.getArticulo().getProducto().getNombre();
        this.talla = stock.getArticulo().getTalla().getTalla();
        this.color = stock.getArticulo().getColor().getColor();
        this.cantidad = stock.getCantidad();
        this.idAlmacen = stock.getAlmacen().getId();
        this.nombreAlmacen = stock.getAlmacen().getNombre();
    }
}
