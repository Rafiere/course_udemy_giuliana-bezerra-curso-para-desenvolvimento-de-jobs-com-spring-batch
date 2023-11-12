package dev.piccodev.springbatchinicial.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.function.FunctionItemProcessor;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ParImparBatchConfig {

    @Autowired private JobBuilderFactory jobBuilderFactory;
    @Autowired private StepBuilderFactory stepBuilderFactory;

    //Vamos ter um leitor que lerá um número e imprimirá par ou ímpar de acordo com o número que está
    //sendo lido.

    @Bean
    public Job imprimeParImparJob(){
        return jobBuilderFactory.get("imprimeParImparJob")
                .start(imprimeParImparStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    private Step imprimeParImparStep() {

        //Implementação do step baseado em chunk.

        return stepBuilderFactory.get("imprimeParImparStep")
                .<Integer, String>chunk(1) //O primeiro parâmetro é o tipo de dado que será lido e o segundo é o tipo de dado que será escrito. O "1" é o "commit interval".
                                                     //O tamanho do chunk ser "1" significa que a cada item, criaremos uma transação. O custo para criarmos uma transação é alto para o banco de
                                                     //dados, por isso, é importante que o chunk não seja muito pequeno, porém, ele não pode ser muito grande, porque esses dados ficarão
                                                     //em memória, assim, devemos aumentar o máximo possível, porém, sempre pensando na memória e em dividir o processamento em alguns pedaços.
                                                     //Temos que estudar a máquina que executará essa transação para chegarmos em um valor correto.

                .reader(contaAteDezReader()) //O leitor lerá um pedaço de dados.
                .processor(parOuImparProcessor()) //O processador processará o pedaço de dados.
                .writer(imprimeWriter()) //O escritor escreverá o pedaço de dados.
                .build();
    }

    //Abaixo, temos um leitor que lerá um número de 1 a 10.

    //O leitor receberá uma coleção de itens que serão lidos.
    private IteratorItemReader<Integer> contaAteDezReader() {

        List<Integer> numerosDeUmAteDez = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        return new IteratorItemReader<Integer>(numerosDeUmAteDez.iterator());
    }

    //O processador receberá um item de cada vez.
    public FunctionItemProcessor<Integer, String> parOuImparProcessor() {

        //Para cada item, o processador verificará se o item é par ou ímpar.

        return new FunctionItemProcessor<Integer, String>
                (item -> item % 2 == 0 ? String.format("Item %s é Par", item) : String.format("Item %s é Ímpar", item));
    }

    //O escritor receberá a coleção inteira processada.
    public ItemWriter<String> imprimeWriter(){

            //O escritor imprimirá o item.

            return itens -> itens.forEach(System.out::println);
    }
}
