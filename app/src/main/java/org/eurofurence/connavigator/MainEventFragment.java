package org.eurofurence.connavigator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import io.swagger.client.JsonUtil;
import io.swagger.client.model.EventEntry;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainEventFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainEventFragment extends Fragment {
    private EventEntry eventEntry;

    private OnFragmentInteractionListener mListener = new OnFragmentInteractionListener() {
        @Override
        public void onFragmentInteraction(Uri uri) {
            System.out.println(uri);
        }
    };

    TextView eventTitle;

    public MainEventFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param eventEntry Event entry
     * @return A new instance of fragment MainEventFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainEventFragment newInstance(EventEntry eventEntry) {
        MainEventFragment fragment = new MainEventFragment();
        Bundle args = new Bundle();
        args.putString("eventEntry", JsonUtil.serialize(eventEntry));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_event, container, false);
        eventTitle = (TextView) view.findViewById(R.id.eventTitle);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            EventEntry eventEntry = JsonUtil.deserializeToObject(arguments.getString("eventEntry"), EventEntry.class);
            setEventEntry(eventEntry);
        } else {
            System.err.println("Arguments are null");
        }
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setEventEntry(EventEntry eventEntry) {
        this.eventEntry = eventEntry;

        updateLayout();
    }

    private void updateLayout() {
        eventTitle.setText(eventEntry.getTitle());
    }
}
