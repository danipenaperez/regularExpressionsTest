# regularExpressionsTest
simple tests with regular Expressions agains a text chain.


A todo el mundo le da pereza las expresiones regulares, (a mi al que mas), pero bueno vamos a quitarnos el miedo y voy a poner aqui unos ejemplillos sencillos , que luego van creciendo en complejidad (no mucha) pero para que veamos que no son para tanto si te las cuentan bien desde el principio:


Tenemos una cadena de texto a procesar :


String CHAIN = "abcde  %%~~paco123@gmail.comAAbd%%%";


Asi de primeras vemos mucha morralla, vemos un email y simbolos raros...

Vamos a ir testeando expresiones regulares contra esa cadena y asi vamos viendo como podemos ir jugando con ellas y anidandolas

 

El codigo Java para probarlas  es este:

      

        List<String> regExps = Arrays.asList("\\d", "\\d+", etc.....);

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


si nos fijamos en el codigo, cuando una expresion regular se compila y se ejecuta contra una cadena de texto, lo que vamos a ir encontrando son eventos m.find(). Es decir,  internamente va a ir revisandose la cadena caracter a caracter y cuando encuentra algo m.find()->return true y el metodo m.group() nos devuelve lo que acaba de encontrar que cumple nuestra expresion regular.

Por lo tanto el procesamiento es orientado a eventos.




### Empezamos de las mas sencillas para arriba :


Usamos los parentesis para meter dentro nuetras expressiones regulares. Te encuenta que una expresion regular compleja puede estar compuesta por varias subexpresiones dentro de parentesis.


### (\\d)   output ->  [1, 2, 3]
Esta expresion saca uno por uno todos los digitos
Evalua caracter a caracter y si es un digito salta el find() y nos devuelve un grupo que contiene ese digito. Por lo tanto tenemos en la lista final de ocurrencias 3 entradas, con los numeros 1,2,3.


### (\\d+) output -> [123]
Esta expresion es igual va a ir caracter a caracter y si es un numero lo guarda como macheo, pero al tener un "+" le indicamos que si depues de ese digito hay mas digitos que los guarde todos en el mismo grupo, por eso encuentra el 1 y mira el caracter que hay despues , si es otro numero, lo junta con el anterior (12), sigue mirando y encuentra el 3 que tambien machea asi que ya tiene un grupo con 123, como el siguiente caracter es un @ pues da por cerrado el grupo ya que no hay mas coincidencias, entonces la llamada a m.group() devuelve "123".  ¿a que explicado asi es mas sencillo?


### (\\w) output -> [a, b, c, d, e, p, a, c, o, 1, 2, 3, g, m, a, i, l, c, o, m, A, A, b, d]
Esta es igual que la de los digitos, pero con caracteres alfanumericos y underscore. Como no tiene + va a ir sacando caracter a caracter. Si te fijas ha excluido los espacios y lo simbolos "raros" ~% .


### (\\w+) output-> [abcde, paco123, gmail, comAAbd]
Prima hermana de la anterior, pero como lleva el +,cuando encuentra una coincidencia comienza a almacenar en el grupo, y mete en el mismo grupo hasta que no se cumple la condicion. "abcde" se para porque encuentra un espacio asi que no añade mas a a ese grupo y lo devuelve. sigue mirando y lo siguiente es "p" y sigue almacenando "paco123" porque cumple el patron y cuando llega a la @ lo da por cerrado porque no machea.
 

### ([a-z]) output-> [a, b, c, d, e, p, a, c, o, g, m, a, i, l, c, o, m, b, d]
Esta especifica un rango de valores, case sensitive. Por eso hace grupos individuales (no tiene el +) con cada caracter que encuentra dentro del rango de las minusculas. si quieres haz la prueba con ([a-m]) y asi veras que excluye las letras posteriores a la m. Mola!, ya puedes ir jugando con algo.
 

### ([a-z]+) output-> [abcde, paco, gmail, com, bd]
Esta es la misma que antes, pero agrupa mientras se cumpla el patron.
 

### ([a-zA-Z]+) output-> [abcde, paco, gmail, comAAbd]
Esta indica varios grupos de coincidencias, o de la a-z o de la A-Z y ademas me los agrupas si estan seguidos. ¿a que parecia compleja y no era para tanto?
 

### (paco|pedro|com |gmail) output-> [paco, gmail]
Esta especifica un or de un grupò de valores. si es alguno de esos, pues hace un grupo.


### (\\d+{3}) output-> [123]
Esta es un poco mas especial, dice que saque los digitos y que los agrupe, pero que los grupos no los haga mas grandes de 3 caracteres. En el ejemplo encuentra el 123 despues de "paco". si la cadena donde busca fuera "paco4567jose12", lo que nos devolveria solo "456" porque son digitos y los agrupa de 3 en 3, ya que luego esta "jose" que no le vale y por ultimo encuentra "12" pero que como no llega a ser un grupo de 3, pues lo descarta.



### VAMOS A ANIDARLAS

 

### (paco)(\\d) output-> [paco1]
Esta evalua la primera expresion que encuentra entre parentesis, asi que busca a piñon fijo coincidencias con "paco" y si lo encuentra evalua si despues hay un digito (\\d).  En caso de que se cumplan las 2 expresiones nos devuelve un grupo que conteine "paco1".


### (paco)(\\d+) -> [paco123]
Prima hermana de la anterior, pero si te fijas al poner el + en la expresion de los digitos, pues sigue pillando digitos hasta que no se cumpla y nos devuelve todo el chorizo junto "paco123".   "Coño, pues tiene sentido, mola".
 

### (paco)^(\\d) output -> []
Esta mola porque usa la negacion "^". Vemos que busca que tenga "paco" y que despues NO contenga un digito, por eso no devuelve ninguna coincidencia, porque despues de "paco" hay un "1". y entonces lo descarta todo.


### ([a-zA-Z0-9]+) output -> [abcde, paco123, gmail, comAAbd] 
Esta busca que el caracter este entre los margenes a-z o A-Z o 0-9, y si hay varias coincidencias seguidas, que las agrupe y me las devuelva como si fuera un "todo".
 

### ([a-zA-Z]+)(\\d+) output -> [paco123]
Lo mismo, pero despues de agrupar tiene que mirar si hay digitos y si hay varios digitos seguidos que lo agrupe todo.


### ([a-zA-Z]+)(\\d+)@ output -> [paco123@]
Lo mismo que la anterior pero ademas que despues aparezca un @.  Si te fijas , esta expresion lleva ya 3 condiciones (la de las letras, la de los numeros y que justo despues exista un arroba. 

### ([a-zA-Z]+)(\\d+)@([a-z]+) output -> [paco123@gmail]
Lo mismo, pero que ademas, despue del arroba haya una (o varias) minusculas.


### ([a-zA-Z]+)(\\d+)@([a-z]+). output -> [paco123@gmail.]
Lo mismo que la anterior y que despues de las minusculas haya un punto "."

### ([a-zA-Z]+)(\\d+)@([a-z]+).([a-z]{3}) output -> [paco123@gmail.com]
Lo mismo pero que despues del punto haya minusculas y que coga solo 3.


### PERO.... SI ACABAMOS DE HACER UNA EXPRESION REGULAR QUE BUSCA EMAILS VALIDOS EN UN CHORIZO DE TEXTO!!!   Y CASI SIN DESPEINARNOS!!  


Espero que te haya servido este articulo para entender un poco como funciona y perderle el miedo a las expresiones regulares.


