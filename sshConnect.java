System.out.print("Executing Command.. --> ");
String text = null;
Channel channel=session.openChannel("exec");

((ChannelExec)channel).setCommand(command);

channel.setInputStream(null);
((ChannelExec)channel).setErrStream(System.err);

InputStream in=channel.getInputStream();
InputStream stdout = channel.getInputStream();
InputStream stderr = ((ChannelExec) channel).getErrStream();

channel.connect();

byte[] tmp=new byte[1024];
while(true){
  while(in.available()>0){
    int i=in.read(tmp, 0, 1024);
    if(i<0)break;
    System.out.print(new String(tmp, 0, i));
    }

    if(channel.isClosed()){
      if(in.available()>0) continue;
    System.out.println("exit-status: "+channel.getExitStatus());
    break;
    }
  try{Thread.sleep(2000);}catch(Exception ee){}
  }

  if (channel.getExitStatus() != 0) {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stderr));
    StringBuilder builder = new StringBuilder();
    String aux = "";

    while ((aux = bufferedReader.readLine()) != null) {
      builder.append("\n" + aux);
    }
    text = builder.toString();

  } else {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stdout));
    StringBuilder builder = new StringBuilder();
    String aux = "";

    while ((aux = bufferedReader.readLine()) != null) {
      builder.append("\n" + aux);
    }
    text = builder.toString();
  }
    
  if (channel != null) {
    channel.disconnect();
  }

  if (session != null) {
    session.disconnect();
  }
  System.out.println("The output string received: "+text);
  return text;
}
