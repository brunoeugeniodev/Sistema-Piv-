from flask import Blueprint, request, jsonify
from bson.objectid import ObjectId
from app.database import mongo
from app.models import Pivo, Historico_irrigacao 
import requests 

api_bp = Blueprint("api", __name__)

def fetch_fazenda_id(usuario_id):
    GESTOR_API_BASE_URL = "http://localhost:8080/api/sistemas"
    url = f"{GESTOR_API_BASE_URL}/usuarios/{usuario_id}/fazenda"
    
    try:
        response = requests.get(url, timeout=5) 
        response.raise_for_status() 
        return response.json() 
        
    except requests.exceptions.RequestException as e:
        print(f"Erro ao buscar fazenda para usuario_id={usuario_id} na URL: {url} - Erro: {e}") 
        return None

@api_bp.route("/pivos", methods=["POST"])
def create_pivo(): 
    data = request.json
    usuario_id = data.get("usuario_id") 

    if usuario_id is None:
        return jsonify({"error": "usuario_id é obrigatório para criar um pivô"}), 400

    id_fazenda = fetch_fazenda_id(usuario_id)

    if id_fazenda is None: 
        print(f"Erro: Fazenda não encontrada ou erro na busca para usuario_id={usuario_id}")
        return jsonify({"error": "Fazenda não encontrada para o usuário fornecido ou erro na comunicação"}), 404
    
    data["id_fazenda"] = id_fazenda
    
    result = mongo.db.pivo.insert_one(data) 
    data["_id"] = result.inserted_id
    return jsonify(Pivo.to_dict(data)), 201

@api_bp.route("/pivos", methods=["GET"])
def get_pivos():
    pivos = mongo.db.pivo.find()
    return jsonify([Pivo.to_dict(pivo) for pivo in pivos])

@api_bp.route("/pivos/<string:pivo_id>", methods=["GET"])
def get_pivo(pivo_id):
    try:
        pivo = mongo.db.pivo.find_one({"_id": ObjectId(pivo_id)})
        if pivo:
            return jsonify(Pivo.to_dict(pivo))
        return jsonify({"error": "Pivô não encontrado"}), 404
    except Exception as e:
        return jsonify({"error": f"ID de Pivô inválido: {e}"}), 400

@api_bp.route("/pivos/<string:pivo_id>", methods=["PUT"])
def update_pivo(pivo_id): 
    data = request.json
    update_data = {"$set": {key: value for key, value in data.items() if value is not None and key != 'id'}}
    
    result = mongo.db.pivo.update_one({"_id": ObjectId(pivo_id)}, update_data) 

    if result.matched_count: 
        updated_pivo = mongo.db.pivo.find_one({"_id": ObjectId(pivo_id)}) 
        return jsonify(Pivo.to_dict(updated_pivo))
    return jsonify({"error": "Pivô não encontrado"}), 404

@api_bp.route("/pivos/<string:pivo_id>", methods=["DELETE"])
def delete_pivo(pivo_id): 
    result = mongo.db.pivo.delete_one({"_id": ObjectId(pivo_id)}) 
    if result.deleted_count:
        return jsonify({"message": "Pivô deletado com sucesso"}), 200
    return jsonify({"error": "Pivô não encontrado"}), 404
    
@api_bp.route("/historicos", methods=["POST"])
def create_historico(): 
    data = request.json
    result = mongo.db.historico_irrigacao.insert_one(data) 
    data["_id"] = result.inserted_id
    return jsonify(Historico_irrigacao.to_dict(data)), 201

@api_bp.route("/historicos", methods=["GET"])
def get_historicos():
    historicos = mongo.db.historico_irrigacao.find() 
    return jsonify([Historico_irrigacao.to_dict(historico) for historico in historicos])

@api_bp.route("/historicos/<string:historico_id>", methods=["GET"])
def get_historico(historico_id): 
    try:
        historico = mongo.db.historico_irrigacao.find_one({"_id": ObjectId(historico_id)})
        if historico:
            return jsonify(Historico_irrigacao.to_dict(historico))
        return jsonify({"error": "Histórico não encontrado"}), 404
    except Exception as e:
        return jsonify({"error": f"ID de Histórico inválido: {e}"}), 400

@api_bp.route("/historicos/<string:historico_id>", methods=["PUT"])
def update_historico(historico_id):
    data = request.json
    update_data = {"$set": {key: value for key, value in data.items() if value is not None and key != 'id'}}
    result = mongo.db.historico_irrigacao.update_one({"_id": ObjectId(historico_id)}, update_data) 

    if result.matched_count:
        updated_historico = mongo.db.historico_irrigacao.find_one({"_id": ObjectId(historico_id)}) 
        return jsonify(Historico_irrigacao.to_dict(updated_historico))
    return jsonify({"error": "Histórico não encontrado"}), 404

@api_bp.route("/historicos/<string:historico_id>", methods=["DELETE"])
def delete_historico(historico_id):
    result = mongo.db.historico_irrigacao.delete_one({"_id": ObjectId(historico_id)}) 
    if result.deleted_count: 
        return jsonify({"message": "Histórico deletado com sucesso"}), 200
    return jsonify({"error": "Histórico não encontrado"}), 404
