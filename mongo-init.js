db.createUser(
    {
        user: "aituuser",
        pwd: "aitupass",
        roles: [
            { role: "readWrite", db: "aitunetwork" }
        ]
    })