package com.example.apijsonsearch.controller;

import com.example.apijsonsearch.model.Equipo;
import com.example.apijsonsearch.service.SitrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class EquipoController {

    @Autowired
    private SitrackService sitrackService;

    // ----------- FRONT TRADICIONAL (RENDERIZA HTML) ------------

    @GetMapping("/")
    public String mostrarFormularioBusqueda() {
        return "index";  // index.html o index.jsp
    }

    @GetMapping("/buscar")
    public String buscarEquipo(@RequestParam("texto") String texto, Model model) {
        Equipo equipo = sitrackService.buscarEquipoPorTexto(texto);
        if (equipo != null) {
            model.addAttribute("equipo", equipo);
            return "detalle";  // Vista con datos cargados
        } else {
            model.addAttribute("noEncontrado", true);
            model.addAttribute("textoBuscado", texto);
            return "index";
        }
    }

    // ----------- API PARA JAVASCRIPT (ENTREGA JSON) ------------

    @GetMapping("/api/equipo/buscar")
    @ResponseBody
    public ResponseEntity<Equipo> buscarPorTexto(@RequestParam String texto) {
        Equipo equipo = sitrackService.buscarEquipoPorTexto(texto);
        if (equipo != null) {
            return ResponseEntity.ok(equipo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
