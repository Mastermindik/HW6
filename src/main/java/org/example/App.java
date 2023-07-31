package org.example;

import javax.persistence.*;
import java.util.List;
import java.util.Scanner;

public class App {
    static EntityManagerFactory emf;
    static EntityManager em;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            emf = Persistence.createEntityManagerFactory("default");
            em = emf.createEntityManager();
            try {
                while (true) {
                    System.out.println("1: add dish");
                    System.out.println("2: get dish");
                    System.out.println("3: get dish with discount");
                    System.out.print("-> ");
                    String q = sc.nextLine();
                    if (q.equals("1")) {
                        addDish(sc);
                    } else if (q.equals("2")) {
                        getDish(sc);
                    } else if (q.equals("3")) {
                        getDishWithDiscount();
                    } else {
                        return;
                    }
                }
            } finally {
                sc.close();
                em.close();
                emf.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addDish(Scanner sc) {
        System.out.println("Enter dish name: ");
        String name = sc.nextLine();
        System.out.println("Enter dish price: ");
        double price = Double.parseDouble(sc.nextLine());
        System.out.println("Enter dish weight: ");
        double weight = Double.parseDouble(sc.nextLine());
        System.out.println("Is the discount valid: ");
        boolean discount = Boolean.parseBoolean(sc.nextLine());

        em.getTransaction().begin();
        try {
            Dish dish = new Dish(name, price, weight, discount);
            em.persist(dish);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        }
    }

    private static void getDish(Scanner sc) {
        System.out.println("Enter price from: ");
        double priceFrom = Double.parseDouble(sc.nextLine());
        System.out.println("Enter price to: ");
        double priceTo = Double.parseDouble(sc.nextLine());

        try {
            Query query = em.createQuery("SELECT d FROM Dish d WHERE d.price >= " + priceFrom + " AND " + "d.price <= "
                    + priceTo, Dish.class);
            List<Dish> list = (List<Dish>) query.getResultList();
            for (Dish d : list) {
                System.out.println(d);
            }
        } catch (NoResultException e) {
            System.out.println("Dish not found");
            return;
        }

    }

    private static void getDishWithDiscount() {
        Query query = em.createQuery("SELECT d FROM Dish d WHERE d.discount = true", Dish.class);
        List<Dish> list = (List<Dish>) query.getResultList();
        for (Dish d : list) {
            System.out.println(d);
        }
    }
}
