package aitu.network.aitunetwork.service.util;

import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GridFsService {
    private final GridFsTemplate gridFsTemplate;

    public String uploadFile(MultipartFile file) throws IOException {
        ObjectId fileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return fileId.toHexString();
    }

    public GridFSFile getFile(String id) {
        return gridFsTemplate.findOne(new Query(Criteria.where("_id").is(id)));
    }

    public GridFsResource downloadFile(String id) {
        GridFSFile gridFSFile = getFile(id);
        if (gridFSFile == null) {
            throw new EntityNotFoundException(String.format("File with %s not found", id));
        }
        return gridFsTemplate.getResource(gridFSFile);
    }

    public void deleteFile(String id) {
        log.info("id to delete {}", id);
        gridFsTemplate.delete(Query.query(Criteria.where("_id").is(id)));
    }
}
