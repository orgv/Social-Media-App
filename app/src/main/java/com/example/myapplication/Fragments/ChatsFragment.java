package com.example.myapplication.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.UserAdapter;
import com.example.myapplication.Chatlist;
import com.example.myapplication.R;
import com.example.myapplication.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatsFragment extends Fragment {

    RecyclerView recyclerView;
    UserAdapter userAdapter;
    List<User> users;


    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    NavController navController;

    List<Chatlist> usersList;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        FloatingActionButton fab = view.findViewById(R.id.fab_users);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //NavHostFragment navHostFragment = FragmentManager.findFragment(getActivity().findViewById(R.id.chatsFragment_1));

                //navHostFragment.getNavController().navigate(R.id.inChatFragment_1);

                Bundle bundle = new Bundle();
                bundle.putString("isCreatedFromNavComponent", "false");
                Navigation.findNavController(v).navigate(R.id.usersFragment, bundle);
                //Navigation.createNavigateOnClickListener(R.id.inChatFragment, bundle);


            }
        });

        recyclerView = view.findViewById(R.id.users_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){ // for chat in chats

                    Chatlist chatlist = dataSnapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                    // here I got all of my chats! I want to retrieve corresponding users that I am talking with according to this info!
                    // Essentially, usersList array is used to store id references of the users I am talking with!

                }

                displayLastChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void displayLastChats() {
        users = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    for(Chatlist chatlist : usersList){ // for each chat in users I am talking with
                        if(user.getId().equals(chatlist.getId())){ // I want to add users array to the adapter. comparing ID's and if equal retrieiving all of appropriate users' info.
                            users.add(user);
                        }
                    }
                }

                // At this point 'users' array contains all of the DATA,INFO, about the users I am talking with!
                // Not only ID like before(usersList), but also whole info, which we took by assigning to User class variable.

                userAdapter = new UserAdapter(getContext(),users,true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void readChats() {
//        users = new ArrayList<>();
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                users.clear();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    User user = dataSnapshot.getValue(User.class);
//
//
//                    // display 1 user from chats
//                    for (String id : usersList) {
//                        if (user.getId().equals(id)) {
//                            if (users.size() != 0) {
//                                for (User userElement : users) {
//                                    if (!user.getId().equals(userElement.getId()))
//                                        users.add(userElement);
//                                }
//                            } else {
//                                users.add(user);
//                            }
//                        }
//                    }
//
//                }
//                userAdapter = new UserAdapter(getContext(), users, true);
//                recyclerView.setAdapter(userAdapter);
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

}


/*

String choice = dropdown.get

 */