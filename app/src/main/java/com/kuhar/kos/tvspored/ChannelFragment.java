package com.kuhar.kos.tvspored;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChannelFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChannelFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChannelFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "url";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public ChannelFragment() {
        // Required empty public constructor
    }

    public static ChannelFragment newInstance(String url) {
        ChannelFragment fragment = new ChannelFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new AsyncTask<String, Void, ArrayList>() {

            @Override
            protected ArrayList doInBackground(String... ItemLink) {
                XMLParse xml = new XMLParse();
                ArrayList seznamOddaj = xml.getSeznamOddaj(ItemLink[0]);
                return seznamOddaj;
            }

            private ProgressDialog mDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                mDialog = new ProgressDialog(getActivity());
                mDialog.setMessage("Nalaganje");
                mDialog.show();
                mDialog.setCancelable(false);
            }

            @Override
            protected void onPostExecute(final ArrayList seznamOddaj) {
                super.onPostExecute(seznamOddaj);
                mDialog.dismiss();

                Collections.sort(seznamOddaj);
                final ArrayList programmeData = new ArrayList();
                for (int i = 0; i < seznamOddaj.size() - 1; i++) {
                    String title = ((ProgrammeData) seznamOddaj.get(i)).title;
                    title = trimTitle(title, 26);
                    String desc = ((ProgrammeData) seznamOddaj.get(i)).description;
                    desc = trimString(desc, 10);
                    String startTime = ((ProgrammeData) seznamOddaj.get(i)).getStartTime();
                    startTime = startTime.substring(0, startTime.lastIndexOf(':'));
                    programmeData.add(new MainItem(title,
                            desc,
                            startTime,
                            ((ProgrammeData) seznamOddaj.get(i)).endTime));
                }

                CustomAdapter adapter = new CustomAdapter(getActivity(), programmeData);
                ListView listView = (ListView) getView().findViewById(R.id.tabListView);
                if (listView != null){
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String titleDialog = ((ProgrammeData) seznamOddaj.get((int) id)).title;
                            String descDialog = ((ProgrammeData) seznamOddaj.get((int) id)).description;
                            if (descDialog == null){
                                descDialog = "Ni opisa";
                            }
                            new AlertDialog.Builder(getContext())
                                    .setTitle(titleDialog)
                                    .setMessage(descDialog)
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .show();
                        }
                    });
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                                       int pos, long id) {
                            TextView tv = (TextView) view.findViewById(R.id.value);
                            tv.setText(((ProgrammeData) seznamOddaj.get((int) id)).description);
                            return true;
                        }
                    });
                }
            }
        }.execute(getArguments().getString(ARG_PARAM1));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tabbed, null);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private static String trimString(String string, int length) {
        if (string == null || string.trim().isEmpty()) {
            return "Ni opisa";
        }
        int countSpaces = string.length() - string.replace(" ", "").length();
        if (length > countSpaces) {
            return string;
        }

        return string.substring(0, nthIndexOf(string, ' ', length)) + " ...";
    }

    private static int nthIndexOf(String text, char needle, int n) {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == needle) {
                n--;
                if (n == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private String trimTitle(String title, int n){
        if (title != null){
            if (title.length() < n){
                return title;
            }
            return title.substring(0, n) + "...";
        }
        return "Ne morem parsat";
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}