package org.example.practicahibernate.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.practicahibernate.dao.LibroDAO;
import org.example.practicahibernate.model.Libro;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class LibroController {
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtAutor;
    @FXML
    private TextField txtAnio;
    @FXML
    private TextField txtPrecio;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCargar;
    @FXML
    private TableView<Libro> tblLibros;
    @FXML
    private TableColumn<Libro, Integer> colId;
    @FXML
    private TableColumn<Libro, String> colTitulo;
    @FXML
    private TableColumn<Libro, String> colAutor;
    @FXML
    private TableColumn<Libro, String> colAnio;
    @FXML
    private TableColumn<Libro, Double> colPrecio;

    private final LibroDAO libroDAO = new LibroDAO();

    private Long idSeleccionado = null;

    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colAnio.setCellValueFactory(new PropertyValueFactory<>("anioPublicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        tblLibros.getSelectionModel().selectedItemProperty().addListener((
                obs, oldValue, newValue) -> {
            if (newValue != null) {
                selectLibro(newValue);
            }else{
                idSeleccionado = null;
                actualizarBotones();
            }
        });
        actualizarBotones();
        cargarLibros();
    }


    private void selectLibro(Libro libro) {
        idSeleccionado = libro.getId();
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtAnio.setText(String.valueOf(libro.getAnioPublicacion()));
        txtPrecio.setText(String.valueOf(libro.getPrecio()));
        actualizarBotones();
    }

    private void actualizarBotones() {
        boolean sinSeleccio = idSeleccionado == null;
        btnModificar.setDisable(sinSeleccio);
        btnGuardar.setDisable(!sinSeleccio);
    }

    private Libro obtenerLibroFormulario() {
        String titulo = txtTitulo.getText();
        String autor = txtAutor.getText();
        int anio = Integer.valueOf(txtAnio.getText());
        BigDecimal precio = BigDecimal.valueOf(Double.valueOf(txtPrecio.getText()));
        return new Libro(titulo, autor, anio, precio);
    }

    @FXML
    private void guardarLibro(){
        try{
            Libro libro = obtenerLibroFormulario();
            libroDAO.guardar(libro);
            limpiarFormulario();
            cargarLibros();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    @FXML
    private void modificarLibro(){
        try{
            Libro libro = obtenerLibroFormulario();
            libro.setId(idSeleccionado);
            boolean modificado = LibroDAO.modificar(libro);
            if(!modificado){
                //Alert para indicar que hubo un error a nivel de transaccion
            }
            limpiarFormulario();
            cargarLibros();

            //alert para indicar que la modificacion fue exitosa

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void eliminarLibro(){
        try{
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmacion de eliminacion de datos");
            confirmacion.setHeaderText("Eliminar eliminar libro");
            confirmacion.setContentText("¿Esta seguro que desea eliminar el registro seleccionado?");
            Optional<ButtonType> result = confirmacion.showAndWait();
            if(result.isEmpty() || result.get() != ButtonType.OK){
                return;
            }

            boolean eliminado = libroDAO.eliminar(idSeleccionado);
            if(!eliminado) {

            }
            limpiarFormulario();
            cargarLibros();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    @FXML
    private void cargarLibros(){
        try{
            List<Libro> libros = libroDAO.listar();
            limpiarFormulario();
            tblLibros.setItems(
                    FXCollections.observableArrayList(libros)

            );

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void limpiarFormulario(){
        tblLibros.getSelectionModel().clearSelection();
        idSeleccionado = null;
        txtTitulo.clear();
        txtAutor.clear();
        txtAnio.clear();
        txtPrecio.clear();
        actualizarBotones();
    }
}
