package net.chrisrichardson.asyncpojos.futures;


public interface CompletionCallback<T> {

	void onCompletion(Outcome<T> outcome);
	
}
