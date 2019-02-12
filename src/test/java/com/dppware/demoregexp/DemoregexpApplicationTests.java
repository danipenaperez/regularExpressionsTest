package com.dppware.demoregexp;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.assertj.core.matcher.AssertionMatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoregexpApplicationTests {

	private static final Logger log = LoggerFactory.getLogger(DemoregexpApplicationTests.class);
	
	@Test
	public void contextLoads() {
	}
	
	
	 /**
     * busca palabras en la cadena, para cada una hace un find , se pueden anidar palabras con pipes. busca Match exactos
     */
	@Test
    public void obtainOcurrencesOfASpecifiedWord(){
    	String CHAIN = "123PACO24543PEDRO4564**sdfgPACO3453";
    	
    	String pattern = "PACO|PEDRO|3453|1234";
    	Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(CHAIN);
        List<String> matches = new ArrayList<>();
        while(m.find()) {
        	String found= m.group();
        	System.out.println("found   match:" +found); //verbose
        	matches.add(m.group());
        }
        assertThat(matches, hasItem("PACO"));
        assertThat(matches, not(hasItem("paco")));//case sensitive not
        assertThat(matches, hasItem("PEDRO"));
        assertThat(matches, not(hasItem("JOSELUIS")));//JOSE LUIS not exist on original chain
        assertThat(matches, not(hasItem("1234")));//the last pattern is "1234", but not exists ono original chain
        
        
    }
    
    /**
     * busca letras alfanumericas y underscore
     * 
     */
	@Test
    public void obtaincOnlyCharsAlphanumeric(){
    	String CHAIN = "123AaBb +]?& _";
    	String pattern = "\\w";
    	Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(CHAIN);
        List<String> matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, hasItem("1"));//Match alphanumeric
        assertThat(matches, hasItem("2"));//Match alphanumeric
        assertThat(matches, hasItem("A"));//Match alphanumeric
        assertThat(matches, hasItem("a")); //Match alphanumeric
        assertThat(matches, hasItem("_")); //Match alphanumeric
        assertThat(matches, not(hasItem(" ")));//not alphanumeric
        assertThat(matches, not(hasItem("+")));//not alphanumeric
        assertThat(matches, not(hasItem("]")));//not alphanumeric
        assertThat(matches, not(hasItem("&")));//not alphanumeric
        assertThat(matches, not(hasItem("?")));//not alphanumeric
    }
	
	
	/**
     * con el + busca la coincidencia y sigue agrupando hasta que no se cumpla, no la toma como un grupo unico, si pusieramos solo \\w haría un grupo distinto para cada caracter
     * 
     */
	@Test
    public void matchAndGroup(){
    	String CHAIN = "123cas333miperro%%esta//loco";
    	String pattern = "\\w+";
    	Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(CHAIN);
        List<String> matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, hasItem("123cas333miperro"));
        assertThat(matches, hasItem("esta"));
        assertThat(matches, hasItem("loco"));
        assertThat(matches, hasSize(3)); 
        
    }
	
	/**
     * Busca solo caracteres entre (a-z) minuscula, y hace un grupo con cada caracter
     * 
     */
	@Test
    public void matchByOnlyaToz(){
    	String CHAIN = "123cas333miperro%%esta//loco";
    	String pattern = "[a-z]"; //GROUP INDIVIDUAL and case sensitive
    	Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(CHAIN);
        List<String> matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, not(hasItem("1")));
        assertThat(matches, not(hasItem("2")));
        assertThat(matches, not(hasItem("%")));
        assertThat(matches, not(hasItem("//")));
        assertThat(matches, hasItem("c"));//case sensitive
        assertThat(matches, hasItem("a"));
        assertThat(matches, hasItem("s"));
        assertThat(matches, hasItem("m"));
        assertThat(matches, hasItem("i"));
        
        //MODE 2,  group by patter and change event (+)
        pattern = "[a-z]+"; //GROUP UNTIL THE NEXT OCURRENCE
        p = Pattern.compile(pattern);
        m = p.matcher(CHAIN);
        matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, not(hasItem("1")));
        assertThat(matches, not(hasItem("2")));
        assertThat(matches, not(hasItem("%")));
        assertThat(matches, not(hasItem("//")));
        assertThat(matches, hasItem("cas"));
        assertThat(matches, hasItem("miperro"));
        assertThat(matches, hasItem("esta"));
    }
	
	/**
     * Va a sacar los numeros sin agrupar, pero solo desde el 1 al 5, por eso el 9 no lo va a sacar
     * Pero si agrupamos , sacara cadenas de numeros que cada uno de sus digitos coincida con el rango que expecifica la expresion
     * 
     */
	@Test
    public void matchAndGroup2(){
    	String CHAIN = "123cas333miperro99%%esta//loco";
    	String pattern = "[1-5]";
    	Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(CHAIN);
        List<String> matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, not(hasItem("9")));
        assertThat(matches, hasItem("1"));
        
        pattern = "[1-5]+";
    	p = Pattern.compile(pattern);
        m = p.matcher(CHAIN);
        matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, not(hasItem("9")));
        assertThat(matches, not(hasItem("99")));
        assertThat(matches, hasItem("123"));
        assertThat(matches, hasItem("333"));
        
    }
	
	/**
     * Va a sacar las coincidencias de "cas" seguido de un numero entre el 1 y 5.
     * 
     */
	@Test
    public void matchAndGroup3(){
    	String CHAIN = "123cas3456miperro99%%esta//loco";
    	String pattern = "(cas[1-5])"; //los parentensis indican grupo de busqueda
    	Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(CHAIN);
        List<String> matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, hasItem("cas3")); //
        
        pattern = "(cas[1-5]+)";
    	p = Pattern.compile(pattern);
        m = p.matcher(CHAIN);
        matches = new ArrayList<>();
        while(m.find()) {
        	System.out.println(m.group());
        	matches.add(m.group());
        }
        assertThat(matches, hasItem("cas345")); //El 6 no lo va a meter y ya es un evento de fin de grupo
        
    }
	
	
	/**
     * Va a sacar las coincidencias de "cas" seguido de un numero entre el 1 y 5.
     * 
     */
	@Test
    public void matchAndGroup4(){
    	String CHAIN = "abcde  %%~~paco123@gmail.comAAbd%%%";
    	
    	//available RegExp patterns
    	List<String> regExps = Arrays.asList(	
		"(\\d)", //Saca uno por uno todos los digitos
    	"(\\d+)", //Saca todos los digitos (si estan consecutivos los agrupa "+")
    	"(\\w)",//Saca caracteres alfanumericos y undescore
    	"(\\w+)",//Saca caracteres alfanumericos y undescor (si estan seguidos los agrupa "+")
    	"([a-z])",//hace un grupo con cada caracter individual que este dentro del rango (a-z) case sensitive
    	"([a-z]+)",//igual que el anterior, (si estan seguidos los agrupa)
    	"([a-zA-Z]+)",//igual que el anterior pero acepta mayusculas (si estan consecutivos los agrupa)
    	"(paco|pedro|com |gmail)", //Si encuentra alguna de esas, la almacena
    	"(\\d+{3})", //Busca digitos, los agrupa, pero en grupos de 3,si no hay 3 no machea 
    	///MAS COMPLEJAS
    	"(paco)(\\d)",//Saca todos los paco que el siguiente caraceter sea un digito
    	"(paco)(\\d+)",//igual que la anterior, pero si hay varios caracteres lo mete todo en el mismo saco
    	"(paco)^(\\d)", //saca los paco que no esten seguidos de un digito
    	"([a-zA-Z0-9]+)",//saca grupos que todos sus digitos o esten entre a-z o entre A-Z o 0-9 y si estan seguidos los agrupa
    	"([a-zA-Z]+)(\\d+)", //lo mismo que el anterior, pero ademas tienen que estar seguidos de numeros (si hay varios numeros los mete en el saco tambien)
    	"([a-zA-Z]+)(\\d+)@", //lo mismmo que el anterior, pero ademas tiene que haber una @
    	"([a-zA-Z]+)(\\d+)@([a-z]+)",//lo mismo pero ademas depues de la @ hay una o varias minusculas		
    	"([a-zA-Z]+)(\\d+)@([a-z]+).",//lo mismo pero ademas tiene que haber un punto despues
    	"([a-zA-Z]+)(\\d+)@([a-z]+).([a-z]{3})"//lo mismo pero despues del punto tiene que haber 3 minusculas
    			);
    	
    	//String pattern = "([a-zA-Z]+)(\\d+)@([a-z]+).([a-z]{3})"; //los parentensis indican grupo de busqueda
    	Pattern p;
        Matcher m;
        List<String> matches = new ArrayList<>();;
        for(String regExp: regExps) {
        	matches.clear();
        	m = Pattern.compile(regExp).matcher(CHAIN);
        	while(m.find()) {
            	matches.add(m.group());
            }
            //log.info("{} -> {}", regExp, matches);
            System.out.println(String.format("%s -> %s", regExp, matches));
        }
        
    }

}

