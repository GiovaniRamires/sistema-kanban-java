package br.edu.projetokanban.controller;
import br.edu.projetokanban.view.MenuPrincipal;


public class KanbanController {

    public void iniciar() {
        MenuPrincipal menu = new MenuPrincipal();
        menu.exibir();
    }
}
