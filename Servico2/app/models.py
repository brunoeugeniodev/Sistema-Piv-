from app.database import mongo 
from bson.objectid import ObjectId 

class Pivo:
    @staticmethod
    def to_dict(pivo_doc):
        formatted_pivo = {
            "nome_pivo": pivo_doc.get("nome_pivo"),
            "status_operacao": pivo_doc.get("status_operacao"),
            "modelo": pivo_doc.get("modelo"),
            "id_fazenda": pivo_doc.get("id_fazenda"), 
        }
        if "_id" in pivo_doc and isinstance(pivo_doc["_id"], ObjectId):
            formatted_pivo["id"] = str(pivo_doc["_id"])
        return formatted_pivo
        
class Historico_irrigacao:
    @staticmethod
    def to_dict(historico_doc): 
        formatted_historico = {
            "nome_historico": historico_doc.get("nome_historico"),
            "status_operacao_registrado": historico_doc.get("status_operacao_registrado"),
            "observacoes": historico_doc.get("observacoes"),
            "pivos": historico_doc.get("pivos"), 
        }
        if "_id" in historico_doc and isinstance(historico_doc["_id"], ObjectId):
            formatted_historico["id"] = str(historico_doc["_id"])
        
        return formatted_historico
