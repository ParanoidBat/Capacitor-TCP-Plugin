export interface TCPPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
