package br.com.caelum.carangos.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

@Resource
public class LanceController {
	
	private Result result;

	public LanceController(Result result) {
		this.result = result;
	}
	
	@Post("darLance")
	public void bla(String mensagem) {
		Sender sender = new Sender("????");
		Message message = new Message.Builder().addData("message", mensagem).build();
		
		List<String> devices = new ArrayList<String>();
		
		try {
			MulticastResult results = sender.send(message, devices, 5);
			
			for (com.google.android.gcm.server.Result result : results.getResults()) {
				System.out.println("MensageId: "+result.getMessageId());
			}
		} catch (IOException e) {
			result.nothing();
		}
	}

}
