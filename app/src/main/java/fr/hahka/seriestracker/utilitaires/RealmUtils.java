package fr.hahka.seriestracker.utilitaires;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Created by thibautvirolle on 26/01/15.
 * Classe regroupant les utilitaires Realm génériques
 */
public class RealmUtils {



    /*
     * isUnique(Context, Class, int)
     * Context : context nécessaire à la création d'un objet Realm
     * Class : Classe dérivant de RealmObject, correspondant à une table de la base
     * int : id dont on veut trouver le nombre d'occurences
     *
     * Utilité : vérifier l'unicité d'un élément d'une table Realm
     *
     * return : true si unicité respectée (nb occurence <=1)
     */
    public static int findById(Context pContext, Class pClass, int pId) {

        Realm realm = Realm.getInstance(pContext);

        RealmQuery query = realm.where(pClass)
                .equalTo("id", pId);

        return query.findAll().size();

    }

    /*
     * isUnique(Context, Class, int)
     * Context : context nécessaire à la création d'un objet Realm
     * Class : Classe dérivant de RealmObject, correspondant à une table de la base
     * int : id dont on veut vérifier l'unicité
     *
     * Utilité : vérifier l'unicité d'un élément d'une table Realm
     *
     * return : true si unicité respectée (nb occurence <=1)
     */
    public static boolean isUnique(Context pContext, Class pClass, int pId) {

        return findById(pContext, pClass, pId) <= 1;

    }


    /*
     * isUnique(Context, Class, int)
     * Context : context nécessaire à la création d'un objet Realm
     * Class : Classe dérivant de RealmObject, correspondant à une table de la base
     * int : id dont on veut vérifier l'unicité
     *
     * Utilité : vérifier qu'un élément d'une table Realm existe
     *
     * return : true si élément recherché présent (nb occurence >=1)
     */
    public static boolean exists(Context pContext, Class pClass, int pId) {

        return findById(pContext, pClass, pId) >= 1;

    }







    public static void clearTable(Context pContext, Class pClass){

        Realm realm = Realm.getInstance(pContext);

        realm.beginTransaction();
        realm.clear(pClass);
        realm.commitTransaction();

    }




}
