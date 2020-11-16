/*Exercício 1
David Stefano Aranda da Silva
Matheus Silva Viana
Paolo Angelo Martins Zilioti
Rafael Veloso Lino de Souza
Iraí Fernandes Daniele

*/

package edu.br.impacta.Exercicio1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class Exercicio1 {

    private static final ExecutorService executor = Executors.newWorkStealingPool(5);

    private static Integer getNumber()
    {
       return  (int)(( Math.random()+1)*100 );
    }

    private static Future<List<Integer>> getNumberListAsync() {
        List<Integer>  NumberList = new ArrayList<>();

        return executor.submit(() -> {
            while(NumberList.size()<5)  {
                try {
                    Integer Number = getNumber();
                    if(!NumberList.contains(Number)) {
                        NumberList.add(getNumber());
                        System.out.println("Number:" + String.valueOf(Number));
                        Thread.sleep(500);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return NumberList;
        });
    }

    public static void playNumberListAsync() throws InterruptedException {
        var futureList = new HashSet<Future<List<Integer>>>();
            for(int i = 0; i<5; i++ ) {
                futureList.add(getNumberListAsync());
            }

            while (futureList.stream().anyMatch(x -> !x.isDone())) {
                System.out.println("Still calculating...");
                Thread.sleep(100);
            }


        var newList =futureList
                .stream()
                .map(list ->{
                    try{
                    return list.get();
                        }
                    catch(InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return new ArrayList<Integer>();
                })
                .collect(Collectors.flatMapping(
                    Collection::stream,
                    Collectors.toList()));


        executor.shutdown();
        newList.forEach(System.out::println);
    }
}
