package dev.piccodev.springbatchinicial.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    //Um job é uma tarefa que encapsulará uma rotina.
    @Bean
    public Job imprimeOlaJob2(){
        return jobBuilderFactory
                .get("imprimeOlaJob2")
                .start(imprimeOlaStep2())
                .incrementer(new RunIdIncrementer()) //Esse incrementer adicionará um parâmetro para cada execução, permitindo que o job seja executado mais de uma vez.
                                                     //Esse parâmetro não permitirá que o "job" retorne o processamento caso necessário.
                .build();
    }

    public Step imprimeOlaStep2() {

        return stepBuilderFactory.get("imprimeOlaStep2")
                .tasklet(imprimeOlaTasklet2(null)) //Aqui temos o tipo do step. Nesse caso, um "tasklet" é uma tarefa simples.
                .build();
    }

    @StepScope //Para que possamos acessar o "jobParameters", temos que inserir esse método no contexto de step.
    @Bean //Para que consigamos obter dados do contexto da aplicação.
    public static Tasklet imprimeOlaTasklet2(@Value("#{jobParameters['nome']}") String nome) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("Olá, " + nome);
                return RepeatStatus.FINISHED; //Estamos dizendo que a tarefa terminará.
            }
        };
    }
}
