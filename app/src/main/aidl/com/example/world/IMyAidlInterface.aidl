// IMyAidlInterface.aidl
package com.example.world;
import com.example.world.Person;
import com.example.newclient.Callback;

// Declare any non-default types here with import statements

interface IMyAidlInterface {
   List<Person> addPerson(in Person person);
   void registerCallback(Callback callback);
   void unregisterCallback(Callback callback);
}