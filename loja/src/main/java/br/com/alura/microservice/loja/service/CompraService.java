package br.com.alura.microservice.loja.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.alura.microservice.loja.client.FornecedorClient;
import br.com.alura.microservice.loja.dto.CompraDTO;
import br.com.alura.microservice.loja.dto.InfoFornecedorDTO;
import br.com.alura.microservice.loja.dto.InfoPedidoDto;
import br.com.alura.microservice.loja.modelo.Compra;

@Service
public class CompraService {
	
	@Autowired
	private FornecedorClient fornecedorClient;
	
	private static final Logger LOG = LoggerFactory.getLogger(CompraService.class);
	
//	@Autowired
//	private RestTemplate client;
//	
//	@Autowired
//	private DiscoveryClient eurekaClient;

//	public void realizaCompra(CompraDTO compra) {
//		
//		//Criando um Client HTTP
//		//Argumentos de exchange() são: path, o verbo, informacoes, recptor da resposta.
//		//A resposta cairá no exchange.
//		
//		//RestTemplate client = new RestTemplate(); QUEM VAI CRIAR ESSE OBJ AGORA SEŔA O SPRING, PARA PODERMOS USAR O EUREKA!
//		ResponseEntity<InfoFornecedorDTO> exchange =  client.exchange("http://fornecedor/info/"+compra.getEndereco().getEstado(),
//				HttpMethod.GET, null, InfoFornecedorDTO.class);
//		
//		//Pegando todas instancias de fornecedor. QUem faz isso é o Ribbon.
//		eurekaClient.getInstances("fornecedor").stream()
//		.forEach(fornecedor -> {
//			System.out.println("localhost:"+ fornecedor.getPort());
//		});
//		
//		System.out.println(exchange.getBody().getEndereco());
//	}
	
	//Vamos agora usar o Feign no lugar do RestTemplate, pois este já usa o Ribbon por padrão.
	public Compra realizaCompra(CompraDTO compra) {
		
		LOG.info("Buscando informações do fornecedor do estado de {}", compra.getEndereco().getEstado());
		InfoFornecedorDTO infoPorEstado = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
		
		LOG.info("realizando um pedido");
		InfoPedidoDto pedido = fornecedorClient.realizaPedido(compra.getItens());
		
		System.out.println(infoPorEstado.getEndereco());
		
		Compra compraSalva = new Compra();
		compraSalva.setPedidoId(pedido.getId());
		compraSalva.setTempoDePreparo(pedido.getTempoDePreparo());
		compraSalva.setEnderecoDestino(compra.getEndereco().toString());
	
		return compraSalva;
	}

}
