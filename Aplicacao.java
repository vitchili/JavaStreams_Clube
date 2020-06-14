package Clube;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Aplicacao {

	public static void main(String[] args) {
		Usuario socio1 = new Socio(705246);
		Usuario visitante1 = new Visitante(65);
		Usuario visitante2 = new Visitante(20);
		Usuario socio2 = new Socio(215402);
		
		AcessoClube ac = new AcessoClube();
		ac.entrar(visitante1);
		ac.entrar(visitante2);
		ac.entrar(socio1);
		ac.entrar(socio2);
		
		
		//Verifica os acessos dos visitantes na data informada
		System.out.println(ac.controleVisitantesDia("2020-06-13"));//Entrada: ano-mes-dia
		// Verifica os socios e visitantes em todas as datas.
		System.out.println(ac.acessoGeral());
		// Como no exemplo, todos usuarios entraram hoje, todos sao incluidos no filtro de acesso. Mude o intervalo para testar. Retorna numero de acessos do clube.
		System.out.println(ac.verificaIntervalo("2020-05-14", "2020-06-14"));
	}

}
class AcessoClube {
	private ArrayList<Usuario> listaVisitantes = new ArrayList<Usuario>();
	private ArrayList<Usuario> listaSocios = new ArrayList<Usuario>();
	private ArrayList<Usuario> listaGeral= new ArrayList<Usuario>();
	
	AcessoClube(){
		
	}
	public void entrar(Usuario usuario) { //pode ser socio ou visitante
		if(usuario.getTipoDeMembro().equals("socio")) {
			listaSocios.add(usuario); //adiciona na lista especifica e na geral.
			listaGeral.add(usuario);
		}else {
			listaVisitantes.add(usuario);
			listaGeral.add(usuario);
		}
	}
	public Map<LocalDate, List<Usuario>> controleVisitantesDia(String dia) {
		return ControleVisitantesDia.controleVisitantesDia(listaVisitantes, dia);
	}
	
	public Map<LocalDate, List<Usuario>>  acessoGeral() { //Constroi um mapa de Usuarios Socios e Visitantes agrupados por Data.
		return ControleAcessoGeral.acessoGeral(listaGeral);
	}
	
	public int verificaIntervalo(String dataInicio, String dataFim) {
		return ControleVerificaIntervalo.verificaIntervalo(listaGeral, dataInicio, dataFim);
	}
}

interface Usuario {
	int getNumero();
	LocalDate getData();
	String getTipoDeMembro();
}
class ControleVerificaIntervalo{
	ControleVerificaIntervalo(ArrayList<Usuario> listaGeral, String dataInicio, String dataFim){
		verificaIntervalo(listaGeral, dataInicio, dataFim);
	}
	public static int verificaIntervalo(ArrayList<Usuario> listaGeral, String dataInicial, String dataFinal) {
		LocalDate dataInicialDate = LocalDate.parse(dataInicial);
		LocalDate dataFinalDate = LocalDate.parse(dataFinal);
		List<Usuario> mapaIntervaloEspecifico = listaGeral.stream()
				.filter(e -> e.getData().isBefore(dataFinalDate) && e.getData().isAfter(dataInicialDate)) //Compara dada e retorna os usuarios cujo filter for menor que a data final e maior que a data inicial
				.collect(Collectors.toList());
		return mapaIntervaloEspecifico.size();
	}
}
class ControleAcessoGeral {
	ControleAcessoGeral(ArrayList<Usuario> listaGeral){
		acessoGeral(listaGeral);
	}
	public static Map<LocalDate, List<Usuario>>  acessoGeral(ArrayList<Usuario> listaGeral) { //Constroi um mapa de Usuarios Socios e Visitantes agrupados por Data.
		Map<LocalDate, List<Usuario>> mapaVisitaGeral = listaGeral.stream()
				.collect(Collectors.groupingBy(e -> e.getData()));
		
		return mapaVisitaGeral;
	}
}
class ControleVisitantesDia{
	ControleVisitantesDia(ArrayList<Usuario> listaVisitantes, String dia){
		controleVisitantesDia(listaVisitantes, dia);
	}
	public static Map<LocalDate, List<Usuario>> controleVisitantesDia(ArrayList<Usuario> listaVisitantes, String dia) { //Constroi um mapa de Usuarios Visitantes agrupados por Data.
		LocalDate dataProcurada = LocalDate.parse(dia);
		Map<LocalDate, List<Usuario>> mapaVisita = listaVisitantes.stream()
				.filter(e -> e.getData().equals(dataProcurada))
				.collect(Collectors.groupingBy(e -> e.getData()));
		return mapaVisita;
	}
}
class Socio implements Usuario{
	private int numero; //numero de socio
	private LocalDate dataEntrada;
	private String tipo = "socio";
	
	public Socio(int numero){
		this.numero = numero;
		this.dataEntrada = LocalDate.now();
	}
	@Override
	public int getNumero() {
		return this.numero;
	}
	@Override
	public LocalDate getData() {
		return this.dataEntrada;
	}
	@Override
	public String getTipoDeMembro() {
		return this.tipo;
	}
}


class Visitante implements Usuario {
	private int numero; //numero do convite
	private LocalDate dataEntrada;
	private String tipo = "visitante";
	
	public Visitante(int numero){
		this.numero = numero;
		this.dataEntrada = LocalDate.now();
	}
	@Override
	public LocalDate getData() {
		return this.dataEntrada;
	}
	@Override
	public int getNumero() {
		return this.numero;
	}
	@Override
	public String getTipoDeMembro() {
		return this.tipo;
	}
}
