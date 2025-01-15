import socket
import datetime

# Configurare server
HOST = '127.0.0.1'  # Adresa IP locală
PORT = 12345         # Portul de ascultare

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as server_socket:
    server_socket.bind((HOST, PORT))
    server_socket.listen()
    print(f"Serverul rulează pe {HOST}:{PORT}...")

    while True:
        client_socket, address = server_socket.accept()
        with client_socket:
            print(f"Conexiune stabilită cu {address}")
            current_time = datetime.datetime.now().strftime("%H:%M:%S")
            client_socket.sendall(current_time.encode())
            print(f"Trimis clientului: {current_time}")
