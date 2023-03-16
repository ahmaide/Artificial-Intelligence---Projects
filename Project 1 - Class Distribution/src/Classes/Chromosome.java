package Classes;

import Services.Info;
import Services.ReadFile;

import java.util.ArrayList;

public class Chromosome {
    private ArrayList<int[]> genes;
    private double fitness;
    private double hardFitness;
    private double softFitness;

    public Chromosome() {
        genes = new ArrayList<>();
    }

    public Chromosome(ArrayList<int[]> genes) {
        this.genes = genes;
    }

    public Chromosome(Chromosome chromosome) {
        this.genes = new ArrayList<>();
        assert this.genes != null;
        for (int i = 0; i < chromosome.getGenes().size(); i++) {
            this.genes.add(chromosome.getGenes().get(i));
        }
    }

    public ArrayList<int[]> getGenes() {
        return genes;
    }

    public void setGenes(ArrayList<int[]> genes) {
        this.genes = genes;
    }

    public void addGene(int[] gene) {
        this.genes.add(gene);
    }

    public void setGene(int index, int[] gene) {
        this.genes.set(index, gene);
    }

    public double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public static void calHardFitness(Chromosome chromosome) {
        double fitness = 0;
        fitness = Math.abs(1 - (((Info.getProfessorsConflicts(chromosome)) * 1.0) / ReadFile.courses.size())) * 50;
        chromosome.setHardFitness(fitness);
    }

    public void setHardFitness(double hardFitness) {
        this.hardFitness = hardFitness;
    }

    public static void calSoftFitness(Chromosome chromosome) {
        double fitness = 0;
        fitness += Math.abs(((ReadFile.courses.size() - Info.getCourseSectionsConflicts(chromosome)) * 1.0) / ReadFile.courses.size()) * 15;
        fitness += Math.abs(((ReadFile.courses.size() - Info.getCourseSectionsDaysConflicts(chromosome)) * 1.0) / ReadFile.courses.size()) * 15;
        fitness += Info.getYearCoursesConflicts(chromosome)*1;
        fitness += Info.getYearCoursesSharedDays(chromosome)*1;
        chromosome.setSoftFitness(fitness);
    }

    public void setSoftFitness(double softFitness) {
        this.softFitness = softFitness;
    }

    public double getHardFitness() {
        return hardFitness;
    }

    public double getSoftFitness() {
        return softFitness;
    }

    public static void calculateFitness(Chromosome chromosome) {
        Chromosome.calHardFitness(chromosome);
        Chromosome.calSoftFitness(chromosome);
        chromosome.setFitness(chromosome.getHardFitness() + chromosome.getSoftFitness());
    }
}