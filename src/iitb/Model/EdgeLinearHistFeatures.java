package iitb.Model;
import iitb.CRF.*;
import iitb.Utils.*;

import java.util.*;
import java.io.*;
/**
 *
 * @author Sunita Sarawagi
 *
 */ 

public class EdgeLinearHistFeatures extends FeatureTypes {
    Object fnames[][];
    int histsize;
    boolean allDone;
    int histArr[];
    int histPos;
    EdgeIterator edgeIter;
    Edge edge;
    int edgeNum;
    
    public EdgeLinearHistFeatures(Model m, Object labels[][], int histsize) {
	super(m);
	fnames=labels;
	edgeIter = m.edgeIterator();
	this.histsize = histsize;
	histArr = new int[histsize];
    }
    public boolean startScanFeaturesAt(DataSequence data, int prevPos, int pos) {
	allDone = false;
	edgeIter.start();
	if ((pos < 2) || !edgeIter.hasNext())
	    allDone = true;
	histPos = 1;
	edge = edgeIter.next();
	edgeNum=0;
	return allDone;
    }
    public boolean hasNext() {
	return (histsize > 1) && !allDone;
    }	
    public void next(FeatureImpl f) {
	// zero all other pos..
	for (int i = 0; i < histArr.length; histArr[i++] = -1);
	histArr[histPos] = edge.start;
	f.yend = edge.end;
	f.historyArray = histArr;
	f.val = 1;
	Object fname;
	if (fnames == null) {
	    fname = "H."+histPos;
	} else {
	    fname = fnames[histPos][f.yend];
	}
	histPos++;	
	if (histPos+1 > histsize) {
	    histPos = 1; // the first set is handled by edge features.
	    if (edgeIter.hasNext()) {
		edge = edgeIter.next();
		edgeNum++;
	    } else {
		allDone = true;
	    }
	}
	setFeatureIdentifier(edgeNum*histPos, model.label(f.yend),fname,f);
    }
};
