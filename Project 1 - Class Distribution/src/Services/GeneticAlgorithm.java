package Services;

import Classes.*;


import Classes.Chromosome;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.Comparator;

public class GeneticAlgorithm {
    public static int initialPopulationSize = 2000;
    public static int maximumPopulationSize = 3000;
    public static ArrayList<Chromosome> population = new ArrayList<>();
    public static double bestFitness = 0;
    public static Chromosome bestSolution = null;
    public static int maxIterations = 40000;
    public static double maxFitness = 100;
    public static double crossOverRate = 0.8;
    public static double mutationRate = 0.2;
    public static ArrayList<XYChartClass> charts = new ArrayList<>();

    public static void initializePopulation() {
        bestFitness = 0;
        bestSolution = null;
        population.clear();
        for (int i = 0; i < initialPopulationSize; i++) {
            Chromosome chromosome = Info.generateRandomSolution();
            Chromosome.calculateFitness(chromosome);
            if (bestSolution == null || chromosome.getFitness() > bestSolution.getFitness()) {
                bestSolution = new Chromosome(chromosome);
                bestSolution.setFitness(chromosome.getFitness());
                bestFitness = chromosome.getFitness();
            }
            population.add(chromosome);
        }
    }

    public static Chromosome[] crossOver(Chromosome p1, Chromosome p2) {
        double choice = Math.random();
        if (choice < 0.3)
            return singlePointCrossOver(p1, p2);
        else if (choice >= 0.3 && choice < 0.6)
            return twoPointCrossOver(p1, p2);
        else
            return uniformCrossOver(p1, p2);
    }

    public static Chromosome[] singlePointCrossOver(Chromosome p1, Chromosome p2) {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        int point = Randomization.getRandomNumber(0, (p1.getGenes().size() - 1));

        for (int i = 0; i < point; i++) {
            c1.addGene(p1.getGenes().get(i));
            c2.addGene(p2.getGenes().get(i));
        }

        for (int i = point; i < p1.getGenes().size(); i++) {
            c1.addGene(p2.getGenes().get(i));
            c2.addGene(p1.getGenes().get(i));
        }
        Chromosome.calculateFitness(c1);
        Chromosome.calculateFitness(c2);

        return new Chromosome[]{c1, c2};
    }

    public static Chromosome[] twoPointCrossOver(Chromosome p1, Chromosome p2) {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();
        int point1 = Randomization.getRandomNumber(0, (p1.getGenes().size() - 1));
        int point2 = Randomization.getRandomNumber(0, (p1.getGenes().size() - 1));

        if (point1 > point2) {
            int temp = point1;
            point1 = point2;
            point2 = temp;
        }

        for (int i = 0; i < point1; i++) {
            c1.addGene(p1.getGenes().get(i));
            c2.addGene(p2.getGenes().get(i));
        }

        for (int i = point1; i < point2; i++) {
            c1.addGene(p2.getGenes().get(i));
            c2.addGene(p1.getGenes().get(i));
        }

        for (int i = point2; i < p1.getGenes().size(); i++) {
            c1.addGene(p1.getGenes().get(i));
            c2.addGene(p2.getGenes().get(i));
        }
        Chromosome.calculateFitness(c1);
        Chromosome.calculateFitness(c2);
        return new Chromosome[]{c1, c2};
    }

    public static Chromosome[] uniformCrossOver(Chromosome p1, Chromosome p2) {
        Chromosome c1 = new Chromosome();
        Chromosome c2 = new Chromosome();

        for (int i = 0; i < p1.getGenes().size(); i++) {
            if (Math.random() < 0.5) {
                c1.addGene(p1.getGenes().get(i));
                c2.addGene(p2.getGenes().get(i));
            } else {
                c1.addGene(p2.getGenes().get(i));
                c2.addGene(p1.getGenes().get(i));
            }
        }
        Chromosome.calculateFitness(c1);
        Chromosome.calculateFitness(c2);
        return new Chromosome[]{c1, c2};
    }

    public static void removeFromPopulation() {
        int subPopSize = (int) (population.size() * 0.2);
        int index;
        population.sort(Comparator.comparing(Chromosome::getFitness));
        for (int i = 0; i < (population.size() - maximumPopulationSize); i++) {
            index = Randomization.getRandomNumber(0, (subPopSize - 1));
            population.remove(index);
        }
    }

    public static Chromosome[] select2Parents() {
        population.sort(Comparator.comparing(Chromosome::getFitness).reversed());
        int populationSize = (int)(population.size() * 0.2);
        int index1 = Randomization.getRandomNumber(0, (populationSize - 1));
        int index2 = Randomization.getRandomNumber(0, (populationSize - 1));
        Chromosome p1 = new Chromosome(population.get(index1));
        Chromosome p2 = new Chromosome(population.get(index2));
        Chromosome.calculateFitness(p1);
        Chromosome.calculateFitness(p2);
        return new Chromosome[]{p1, p2};
    }

    public static Chromosome selectRandomSolution() {
        population.sort(Comparator.comparing(Chromosome::getFitness).reversed());
        int populationSize = (int)(population.size() * 0.2);
        int index = Randomization.getRandomNumber(0, (populationSize - 1));
        Chromosome chromosome = new Chromosome(population.get(index));
        Chromosome.calculateFitness(chromosome);
        return chromosome;
    }

    public static Chromosome mutation(Chromosome p1) {
        Chromosome c1 = new Chromosome();
        int mutationIndex = Randomization.getRandomNumber(0, p1.getGenes().size() - 1);
        for (int i = 0; i < p1.getGenes().size(); i++) {
            c1.addGene(p1.getGenes().get(i));
            if (i == mutationIndex) {
                int index3;
                int timeslotID;
                if (Info.getCourseFromID(c1.getGenes().get(i)[0]).getCourseType() == 'c') {
                    index3 = Info.getCoursesSlots().size() - 1;
                    timeslotID = Info.getCoursesSlots().get(index3).getId();
                } else {
                    index3 = Info.getLabsSlots().size() - 1;
                    timeslotID = Info.getLabsSlots().get(index3).getId();
                }
                int lecture = c1.getGenes().get(i)[0];
                c1.setGene(i, new int[]{lecture, timeslotID});
            }
        }
        return c1;
    }

    public static Chromosome mutation1(Chromosome p1){
        Chromosome p = new Chromosome(p1);
        int index1 = Randomization.getRandomNumber(0, (p.getGenes().size() - 1));
        int index2 = Randomization.getRandomNumber(0, (p.getGenes().size() - 1));
        int[] tempGene = p.getGenes().get(index1);
        p.setGene(index2, p.getGenes().get(index1));
        p.setGene(index1, tempGene);
        Chromosome.calculateFitness(p);
        return p;
    }

    public static void solve(ArrayList<Chromosome> chromosomes) {
        int steadyCount = 0;
        double prevBestFittness = -1;
        population = chromosomes;
        for (int i = 0; i < maxIterations && bestSolution.getFitness() < maxFitness; i++) {
            if (i%1000 == 0){
                charts.add(new XYChartClass(i, (int)(bestFitness)));
            }
            if (i % 5000 == 0) {
                System.out.println(i + " - " + bestFitness + " - " + population.size());
                System.out.println(bestSolution.getHardFitness());
                if (prevBestFittness == bestFitness) {
                    steadyCount++;
                    if (steadyCount == 14 && bestFitness < maxFitness && bestSolution.getHardFitness() < 50) {
                        System.out.println("Hi " + bestSolution.getHardFitness());
                        initializePopulation();
                        solve(population);
                        break;
                    }
                }
            }
            Chromosome[] parents;
            parents = select2Parents();
            if (Math.random() < crossOverRate) {
                Chromosome childs[] = crossOver(parents[0], parents[1]);

                if (childs[0].getFitness() > bestSolution.getFitness()) {
                    bestSolution = new Chromosome(childs[0]);
                    Chromosome.calculateFitness(bestSolution);
                    bestFitness = bestSolution.getFitness();
                }
                if (childs[1].getFitness() > bestSolution.getFitness()) {
                    bestSolution = new Chromosome(childs[1]);
                    Chromosome.calculateFitness(bestSolution);
                    bestFitness = bestSolution.getFitness();
                }
                population.add(childs[0]);
                population.add(childs[1]);
            }
            if (Math.random() < mutationRate) {
                Chromosome child = mutation(selectRandomSolution());
                if (child.getFitness() > bestSolution.getFitness()) {
                    bestSolution = new Chromosome(child);
                    Chromosome.calculateFitness(bestSolution);
                    bestFitness = bestSolution.getFitness();
                }
                population.add(child);
            }
            if (population.size() > maximumPopulationSize) {
                removeFromPopulation();
            }
            prevBestFittness = bestFitness;
        }
        System.out.println("Best fitness: " + bestSolution.getFitness());
        System.out.println("Conflicts: " + Info.getProfessorsConflicts(bestSolution));
    }
}