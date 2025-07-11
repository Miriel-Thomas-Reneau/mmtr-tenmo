CREATE OR REPLACE FUNCTION block_admin_accounts()
RETURNS TRIGGER AS '
BEGIN
    IF EXISTS (SELECT 1 FROM tenmo_user WHERE user_id = NEW.user_id AND role = ''ROLE_ADMIN'') THEN
        RAISE EXCEPTION ''Admins are not allowed to have Tenmo accounts.'';
    END IF;
    RETURN NEW;
END;
' LANGUAGE plpgsql;