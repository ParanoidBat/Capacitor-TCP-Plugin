export interface TCPPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
  sendMessage(options: { value: string }): Promise<{ value: string }>;
}
